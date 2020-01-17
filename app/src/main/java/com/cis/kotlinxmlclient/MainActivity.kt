package com.cis.kotlinxmlclient

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Element
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

// xml은 doc 과 sax 가 있다. 여기서는 dom 을 이용한 방식으로 연습
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv.text = ""

        btn.setOnClickListener {
            val thread = NetworkThread()
            thread.start()
        }
    }

    inner class NetworkThread: Thread(){
        override fun run() {
            try {
                val site = "http://10.211.55.4:8080/XmlUse/xml.jsp"

                val url = URL(site)
                val conn = url.openConnection()
                val input = conn.getInputStream()

                val factory = DocumentBuilderFactory.newInstance()
                val builder = factory.newDocumentBuilder()
                val doc = builder.parse(input)

                // xml 전체를 가져온다.
                val root = doc.documentElement

                // item tag들을 list 형태로 가져온다.
                val item_node_list = root.getElementsByTagName("item")

                // item tag 내용을 뽑아낸다.
                for (i in 0 until item_node_list.length) {
                    val item_element = item_node_list.item(i) as Element

                    val data1_node_list = item_element.getElementsByTagName("data1")
                    val data2_node_list = item_element.getElementsByTagName("data2")
                    val data3_node_list = item_element.getElementsByTagName("data3")

                    val data1_node = data1_node_list.item(0) as Element
                    val data2_node = data2_node_list.item(0) as Element
                    val data3_node = data3_node_list.item(0) as Element

                    val data1 = data1_node.textContent
                    val data2 = data2_node.textContent
                    val data3 = data3_node.textContent

                    runOnUiThread {
                        tv.append("data1 : ${data1}\n")
                        tv.append("data2 : ${data2}\n")
                        tv.append("data3 : ${data3}\n\n")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
