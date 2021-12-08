package kr.or.mrhi.mycoinkt.fragment

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kr.or.mrhi.myCoin.model.NewsItem
import kr.or.mrhi.mycoinkt.R
import kr.or.mrhi.mycoinkt.adapter.NewsAdapter
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.util.ArrayList

class FragmentRss : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private val items: ArrayList<NewsItem> = ArrayList<NewsItem>()
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var task: RssFeedTask
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_rss, container, false)
        recyclerView = view.findViewById(R.id.recycler)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)

        newsAdapter = NewsAdapter(items, requireContext())
        recyclerView.adapter=newsAdapter

        //리사이클러의 배치관리자 설정
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager=layoutManager

        //대량의 데이터 추가 작업
        readRss()
        newsAdapter.notifyDataSetChanged()

        //스와이프 갱신하기
        swipeRefreshLayout.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                items.clear() //기사 목록을 담고 있는 List를 다 없애고
                newsAdapter.notifyDataSetChanged()
                readRss() //다시 기사 xml 읽어오고, 스레드 생성
                swipeRefreshLayout.isRefreshing=false
            }
        })
        return view
    }

    //rss xml문서 읽어와서 파싱하는 작업 메소드
    fun readRss() {
        try {
            val url = URL("http://www.coindeskkorea.com/rss/allArticle.xml")
            //실디바이스 중에서 oreo버전 이상에서는 보안강화로 인해 https만 허용하도록 함..

            //스트림역할하여 데이터 읽어오기 : 인터넷 작업은 반드시 퍼미션 작성해야함.
            //Network작업은 반드시 별도의 Thread만 할 수 있다.
            //별도의 Thread 객체 생성
            task = RssFeedTask()
            task.execute(url) //doInBackground()메소드가 발동[thread의 start()와 같은 역할]
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
    } // readRss Method ..

    //이너 클래스
    internal inner class RssFeedTask : AsyncTask<URL?, Void?, String?>() {
        //Thread의 run()메소드와 같은 역할
        override fun doInBackground(vararg urls: URL?): String? { //...는 여러개를 받는 의미, 만약 task.execute(url, url2, url3); 보내면 urls[3]로 받는다.
            //전달받은 URL 객체
            val url = urls[0]

            //해임달(URL)에게 무지개로드(Stream) 열도록..
            try {
                val `is` = url?.openStream()

                //읽어온 xml를 파싱(분석)해주는 객체 생성
                val factory: XmlPullParserFactory = XmlPullParserFactory.newInstance()
                val xpp: XmlPullParser = factory.newPullParser()

                //utf-8은 한글도 읽어오기 위한 인코딩 방식
                xpp.setInput(`is`, "utf-8")
                var eventType: Int = xpp.getEventType()
                var item: NewsItem? = null
                var tagName: String? = null
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    when (eventType) {
                        XmlPullParser.START_DOCUMENT -> {
                        }
                        XmlPullParser.START_TAG -> {
                            tagName = xpp.getName()
                            Log.i("TAG", tagName)
                            if (tagName == "item") {
                                item = NewsItem()
                            } else if (tagName == "title") {
                                xpp.next()
                                if (item != null) item.title=xpp.getText()
                            } else if (tagName == "link") {
                                xpp.next()
                                if (item != null) item.link=xpp.getText()
                            } else if (tagName == "description") {
                                xpp.next()
                                if (item != null) item.desc=xpp.getText()
                            } else if (tagName == "image") {
                                xpp.next()
                                if (item != null) item.imgUrl=xpp.getText()
                            } else if (tagName == "pubDate") {
                                xpp.next()
                                if (item != null) item.date=xpp.getText()
                            }
                        }
                        XmlPullParser.TEXT -> {
                        }
                        XmlPullParser.END_TAG -> {
                            tagName = xpp.getName()
                            if (tagName == "item") {
                                Log.i("SSS", item?.title.toString())
                                //읽어온 기사 한개를 대량의 데이터에 추가
                                if (item != null) {
                                    items.add(item)
                                }
                                item = null

                                //리사이클러의 아답터에게 데이터가
                                //변경되었다는 것을 통지(화면 갱신)
                                //UI변경작업을 하고 싶다면..
                                publishProgress() //onProgressUpdate()라는 메소드 실행
                            }
                        }
                    }
                    eventType = xpp.next()
                } //while

                //파싱 작업이 완료되었다!!
                //테스트로 Toastㄹ 보여주기, 단 별도 스레드는
                //UI작업이 불가! 그래서 runOnUiThread()를 이용했었음.
                //이 UI작업을 하는 별도의 메소드로
                //결과를 리턴하는 방식을 사용
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: XmlPullParserException) {
                e.printStackTrace()
            }
            return "파싱종료" // 리턴 값은 onPostExecute(String s) s에 전달된다.
        } //doInBackground()

        //doInBackground() 작업 도중에
        //publichProgress()라는 메소드를
        //호출하면 자동으로 실행되는 메소드
         override fun onProgressUpdate(vararg values: Void?) {
            super.onProgressUpdate(*values)
            //이곳에서도 UI변경작업이 가능함.
            newsAdapter.notifyItemInserted(items.size) //새로 추가한 것은 마지막에 추가하는 내용
            //전체[notifyDataSetChanged()]를 바꾸면 속도가 좋지않다. 추가한 것만 바꾸자.
        }

        //doInBackground메소드가 종료된 후
        //UI작업을 위해 자동 실행되는 메소드
        //runOnUiThread()와 비슷한 역할
         override fun onPostExecute(result: String?) { //매개 변수 s에 들어오는 값음 doIBackground()의 return 값이다.
            super.onPostExecute(result)

            //리사이클러에서 보여주는 데이터를 가진
            //아답터에게 데이터가 변경되었다고 공지
            //adapter.notifyDataSetChanged();


            //이 메소드 안에서는 UI변경 작업 가능
//            Toast.makeText(getContext(), s+":"+items.size(), Toast.LENGTH_SHORT).show();
        }

    } //RssFeedTask class

    override fun onPause() {
        super.onPause()
        task.cancel(true)
    }
} //MainActivity class ..
