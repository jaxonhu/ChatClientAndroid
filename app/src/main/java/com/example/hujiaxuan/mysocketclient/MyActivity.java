package com.example.hujiaxuan.mysocketclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;


public class MyActivity extends Activity {


    EditText ip;
    EditText editText;
    TextView text;
    String str_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        AlertDialog.Builder builder=new AlertDialog.Builder(MyActivity.this);
        builder.setTitle("请输入昵称：");
        View view= LayoutInflater.from(MyActivity.this).inflate(R.layout.login,null);
        builder.setView(view);

        final EditText name=(EditText)view.findViewById(R.id.et_name);

        builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                str_name=name.getText().toString().trim();
            }
        });
        builder.show();
        setContentView(R.layout.activity_my);
        editText = (EditText) findViewById(R.id.id_et_input);
        text = (TextView) findViewById(R.id.id_tv_msg);

        findViewById(R.id.id_btn_connect).setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View arg0) {

                connect();

            }
        });

        findViewById(R.id.id_btn_send).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                send();
            }
        });
    }

    //-------------------------------------

    Socket socket = null;
    BufferedWriter writer = null;
    BufferedReader reader = null;

    public void connect(){

//            Toast.makeText(MyActivity.this,"成功",Toast.LENGTH_SHORT).show();

            AsyncTask<Void,String,Void> read=new AsyncTask<Void, String, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    try{
                        socket=new Socket("222.20.57.63",30007);
//                        10.12.77.17
//                        192.168.56.101
                        writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        reader=new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
                        publishProgress("@success");
                    }catch (UnknownHostException e){
                        e.printStackTrace();
                        Toast.makeText(MyActivity.this,"无法建立连接",Toast.LENGTH_SHORT).show();
                    }catch (IOException e){
                        e.printStackTrace();
                        Toast.makeText(MyActivity.this,"无法建立连接",Toast.LENGTH_SHORT).show();
                    }

                    try{
                        String line;
                        while((line=reader.readLine())!=null){
                            System.out.println(line);
                            publishProgress(line);
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                    return null;

                }

                @Override
                protected void onProgressUpdate(String... values) {
                    if(values[0].equals("@success")){
                        Toast.makeText(MyActivity.this,"连接成功",Toast.LENGTH_SHORT).show();
                        text.append("已经进入聊天室\n");
                    }else{

                    }


                    super.onProgressUpdate(values);

                }
            };
            read.execute();

    }
    public void send(){
        try{
            text.append(str_name+"说："+editText.getText().toString()+"\n");
            writer.write(editText.getText().toString()+"\n");
            writer.flush();
            editText.setText("");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
