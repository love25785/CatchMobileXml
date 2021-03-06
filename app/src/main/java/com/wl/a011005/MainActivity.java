//存取網路  並使用 Sax  來剖析 xml  檔   ， 使用的語法 在    //
package com.wl.a011005;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity
{
    ListView lv;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.listView);
    }
    public void click1(View v)
    {
        new Thread()
        {
            @Override
            public void run()
            {
                super.run();
                String str_url = "https://www.mobile01.com/rss/news.xml";
                URL url = null;
                try
                {
                    url = new URL(str_url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    InputStream inputStream = conn.getInputStream();
                    InputStreamReader isr = new InputStreamReader(inputStream);
                    BufferedReader br = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    String str;

                    while ((str = br.readLine()) != null)
                    {
                        sb.append(str);
                    }
                    String str1 = sb.toString();
                    Log.d("NET", str1);

                    final MyHandler dataHandler = new MyHandler();  //
                    SAXParserFactory spf = SAXParserFactory.newInstance(); //
                    SAXParser sp = spf.newSAXParser(); //
                    XMLReader xr = sp.getXMLReader(); //
                    xr.setContentHandler(dataHandler); // 以上是建一個SAX
                    xr.parse(new InputSource(new StringReader(str1)));  //把字串丟給SAX 剖析

                    br.close();
                    isr.close();
                    inputStream.close();

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run() //把它裝到listview，第三個引數要輸入DATA:title 是在另一個執行序
                        {
                            adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,dataHandler.titles);
                                    lv.setAdapter(adapter);
                        }
                    });
                }
                catch (MalformedURLException e)
                {
                    e.printStackTrace();
                }
                catch (ProtocolException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                catch (SAXException e)
                {
                    e.printStackTrace();
                }
                catch (ParserConfigurationException e)
                {
                    e.printStackTrace();
                }

            }
        }.start();
    }
}
