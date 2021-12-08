package kr.or.mrhi.mycoinkt

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import kr.or.mrhi.myCoin.model.Transaction
import java.lang.Exception
import java.util.ArrayList

class DBController(context: Context?) : SQLiteOpenHelper(context, "MyCoinDB", null, 1) {
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(
            "CREATE TABLE TransactionTBL (" +
                    "name text, time time, transactions text, quantity text, price text, balance int );"
        )
        sqLiteDatabase.execSQL("CREATE TABLE favoritesTBL ( coinName var(5) PRIMARY KEY);")
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onCreate(sqLiteDatabase)
    }

    fun insertBalanceTransactionTBL(balance: Int) {
        insertTransaction(Transaction("포인트", "add", null, "0.0", "0.0", balance, null))
    }

    fun insertFavorite(name: String) {
        val sqlDB: SQLiteDatabase = getWritableDatabase()
        try {
            sqlDB.execSQL(
                "INSERT INTO favoritesTBL VALUES ('"
                        + name + "')"
            )
        } catch (e: Exception) {
            Log.e("데이터베이스", "faviriteList Insert에러$e")
        } finally {
            sqlDB.close()
        }
    }

    fun insertTransaction(transaction: Transaction) {
        val sqlDB: SQLiteDatabase = getWritableDatabase()
        try {
            sqlDB.execSQL(
                ("INSERT INTO TransactionTBL VALUES ('"
                        + transaction.coinName) + "'," +
                        "(SELECT DATETIME('NOW','localtime')),'"
                        + transaction.transaction.toString() + "', '"
                        + transaction.quantity.toString() + "', '"
                        + transaction.price.toString() + "', '"
                        + transaction.balance.toString() + "')"
            )
        } catch (e: Exception) {
            Log.e("데이터베이스", "insert에러$e")
        } finally {
            sqlDB.close()
        }
    }

    fun getCoinTransaction(coinName: String?): Transaction? {
        val sqlDB: SQLiteDatabase = getReadableDatabase()
        var cursor: Cursor? = null
        var transaction: Transaction? = null
        var myCoinName: String? = "null"
        var transactionTime: String? = "null"
        var transactionForm = "null"
        var currentPrice = "null"
        //현재 산 코인의 값
        var quantity = 0.00
        var price = 0.00
        var tradeQualtity = 0.00
        var balance = 0
        var count = 0
        try {
            cursor = sqlDB.rawQuery("SELECT * FROM TransactionTBL where name = '$coinName';", null)
            while (cursor.moveToNext()) {
                myCoinName = cursor.getString(0)
                transactionTime = cursor.getString(1)
                transactionForm = cursor.getString(2) //사고 팔고
                tradeQualtity = cursor.getString(3).toDouble() //갯수
                currentPrice = cursor.getString(4) //가격
                balance += cursor.getInt(5) //잔액
                if (transactionForm == "buy") {
                    count++
                    price = price + currentPrice.toDouble() * tradeQualtity
                    quantity = quantity + tradeQualtity
                } else if (transactionForm == "sell") {
                    quantity = quantity - tradeQualtity
                }
                //보유수량 ,구매횟수, 구매당시가격, 구매당시가격*갯수
            }
            val avgPrice = price / count
            //                quantity 보유수량 price
            transaction = Transaction(
                myCoinName,
                transactionTime,
                transactionForm,
                quantity.toString(),
                price.toString(),
                balance,
                avgPrice.toString()
            )
        } catch (e: Exception) {
            Log.e("데이터베이스", "select에러3$e")
        } finally {
            sqlDB.close()
            cursor?.close()
        }
        return transaction
    }

    fun getTransactionList(): List<Transaction> {
        val transactionList: MutableList<Transaction> = ArrayList<Transaction>()
        val sqlDB: SQLiteDatabase = getReadableDatabase()
        var cursor: Cursor? = null
        try {
            cursor = sqlDB.rawQuery("SELECT * FROM TransactionTBL;", null)
            while (cursor.moveToNext()) {
                val coinName = cursor.getString(0)
                val transactionTime = cursor.getString(1)
                val transaction = cursor.getString(2)
                val quantity = cursor.getLong(3).toString()
                val price = cursor.getLong(4).toString()
                val balance = cursor.getInt(5)
                transactionList.add(
                    Transaction(
                        coinName,
                        transaction,
                        transactionTime,
                        quantity,
                        price,
                        balance,
                        ""
                    )
                )
            }
        } catch (e: Exception) {
            Log.e("데이터베이스", "select에러 : getTransactionList$e")
        } finally {
            sqlDB.close()
            cursor?.close()
        }
        return transactionList
    }

    fun getFavoritesList(): List<String> {
        val favoritesList: MutableList<String> = ArrayList()
        val sqlDB: SQLiteDatabase = getReadableDatabase()
        var cursor: Cursor?=null
        try {
            cursor = sqlDB.rawQuery("SELECT * FROM favoritesTBL;", null)
            while (cursor.moveToNext()) {
                favoritesList.add(cursor.getString(0))
            }
        } catch (e: Exception) {
            Log.e("데이터베이스", "select에러 : getFavoritesList$e")
        } finally {
            sqlDB.close()
            cursor?.close()
        }
        return favoritesList
    }

    fun deleteFavoritesList(coinName: String) {
        val sqlDB: SQLiteDatabase = getWritableDatabase()
        try {
            sqlDB.execSQL("delete from favoritesTBL where coinName = '$coinName';")
        } catch (e: Exception) {
            Log.e("데이터베이스", "delete에러 : deleteFavoritesList$e")
        } finally {
            sqlDB.close()
        }
    }

    fun getMyWallet(): MutableList<Transaction?> {
        val list: MutableList<String> = ArrayList()
        val transactionList: MutableList<Transaction?> = ArrayList<Transaction?>()
        val sqlDB: SQLiteDatabase = getReadableDatabase()
        var cursor: Cursor? = null
        try {
            cursor = sqlDB.rawQuery("SELECT name From TransactionTBL GROUP BY name ;", null)
            while (cursor.moveToNext()) {
                list.add(cursor.getString(0))
            }
        } catch (e: Exception) {
            Log.e("데이터베이스", "SELECT 에러 : getMyWallet$e")
        } finally {
            sqlDB.close()
            cursor?.close()
        }
        for (str in list) {
            transactionList.add(getCoinTransaction(str))
        }
        return transactionList
    }
}