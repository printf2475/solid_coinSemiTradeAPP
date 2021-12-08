package kr.or.mrhi.myCoin.model

class NewsItem {
    //Getter & Setter Method..
    //멤버 변수가 private이면
    //보통은 객체지향은 정보의 은닉을 해야하기 때문에, private으로 만든다.
    var title: String? = null
    var link: String? = null
    var desc: String? = null
    var imgUrl: String? = null
    var date: String? = null

    constructor() { //혹시 몰라서 매개변수가 없는 것도 만들었다.
    }

    constructor(title: String?, link: String?, desc: String?, imgUrl: String?, date: String?) {
        this.title = title
        this.link = link
        this.desc = desc
        this.imgUrl = imgUrl
        this.date = date
    }
}