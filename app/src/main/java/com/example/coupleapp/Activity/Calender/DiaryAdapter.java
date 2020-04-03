package com.example.coupleapp.Activity.Calender;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.coupleapp.R;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.ViewHolder> {

    private ArrayList<DiaryData> mList;
    private Context context;

    private SharedPreferences sfc;
    private SharedPreferences sf_idx;
    private String name;
    private String couple_idx;
    private String date;

    private static String IP_ADDRESS="13.125.232.78";  // 퍼블릭 IPv4 주소
    private static String TAG = "스토리";          // 로그에 사용할 태그


    public DiaryAdapter(ArrayList<DiaryData> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public DiaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diary,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryAdapter.ViewHolder holder, final int position) {

        //커플 인덱스 번호가져오기
        sf_idx =context.getSharedPreferences("COPLE",MODE_PRIVATE);
        couple_idx = sf_idx.getString("cople_idx","");

        //로그인 저장 정보
        sfc = context.getSharedPreferences("CHAT",MODE_PRIVATE);
        name = sfc.getString("name","");

        holder.tv_title.setText(mList.get(position).getTitle());
        holder.tv_date.setText(mList.get(position).getDate());
        holder.tv_name.setText(mList.get(position).getName());
        holder.tv_time.setText(mList.get(position).getTime());

        if(((mList.get(position).getName()).equals(name))){
            holder.imgbtn_edit.setVisibility(View.VISIBLE);
            holder.imgbtn_delete.setVisibility(View.VISIBLE);
        }else{

            holder.imgbtn_edit.setVisibility(View.GONE);
            holder.imgbtn_delete.setVisibility(View.GONE);
        }
        date = mList.get(position).getDate();

        holder.imgbtn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DiaryModifyActivity.class);
                intent.putExtra("title",mList.get(position).getTitle());
                intent.putExtra("idx",mList.get(position).getIdx());
                intent.putExtra("date",mList.get(position).getDate());
                intent.putExtra("content",mList.get(position).getContent());
                context.startActivity(intent);
            }
        });

        holder.imgbtn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("옵션을 선택하세요.");
                builder.setMessage("일정을 삭제 하시겠습니까?");
                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                        // 그 다음 AsyncTask 객체를 만들어 execute()한다
                        GetData task = new GetData();

                        // execute() 사용 시 DB의 값을 JSON 형태로 가져오는 코드가 적힌 php 파일의 경로를 적어
                        // AsyncTask로 값들을 JSON 형태로 가져올 수 있게 한다
                        task.execute("http://" + IP_ADDRESS + "/diary_delete.php?couple_idx=" + couple_idx + "&idx=" + mList.get(position).getIdx(), "");

                    }
                });
                builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.create().show();
            }
        });

        holder.imgbtn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DiaryDetailActivity.class);
                intent.putExtra("title",mList.get(position).getTitle());
                intent.putExtra("idx",mList.get(position).getIdx());
                intent.putExtra("date",mList.get(position).getDate());
                intent.putExtra("time",mList.get(position).getTime());
                intent.putExtra("name",mList.get(position).getName());
                intent.putExtra("content",mList.get(position).getContent());
                intent.putExtra("focus","1");
                context.startActivity(intent);
            }
        });

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DiaryDetailActivity.class);
                intent.putExtra("title",mList.get(position).getTitle());
                intent.putExtra("idx",mList.get(position).getIdx());
                intent.putExtra("date",mList.get(position).getDate());
                intent.putExtra("time",mList.get(position).getTime());
                intent.putExtra("name",mList.get(position).getName());
                intent.putExtra("content",mList.get(position).getContent());
                intent.putExtra("focus","2");
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title,tv_date,tv_name,tv_time;
        ImageButton imgbtn_comment,imgbtn_delete,imgbtn_edit;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            constraintLayout = itemView.findViewById(R.id.constraintlayout);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            imgbtn_comment = itemView.findViewById(R.id.imgbtn_comment);
            imgbtn_delete = itemView.findViewById(R.id.imgbtn_delete);
            imgbtn_edit = itemView.findViewById(R.id.imgbtn_edit);

        }
    }
    /* HTTPUrlConnection을 써서 POST 방식으로 phpmyadmin DB에서 값들을 가져오는 AsyncTask 클래스 정의 */
    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        /* AsyncTask 작업 시작 전에 UI 처리할 내용을 정의하는 함수 */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // 프래그먼트에 프로그레스 다이얼로그를 띄우고, 값이 가져와지는 동안 기다리라는 메시지를 띄운다
            // 마찬가지로 프래그먼트를 쓰기 때문에 context 대신 getActivity() 사용
            progressDialog = ProgressDialog.show(context,
                    "Please Wait",
                    null,
                    true,
                    true);
        }

        /* AsyncTask 작업 종료 후 UI 처리할 내용을 정의하는 함수 */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // 프로그레스 다이얼로그를 죽이고
            progressDialog.dismiss();

            // doInBackground()의 리턴값이 담긴 result를 버튼 밑 텍스트뷰에 setText()해서 JSON 형태로 받아온 값들을 출력
//            mTextViewResult.setText(result);
            Log.e(TAG, "response - " + result);

            // 결과가 없으면 에러 때문에 못 받아온 거니까 에러 문구를 버튼 밑 텍스트뷰에 출력
            if (result == null) {
//                mTextViewResult.setText(errorString);
            } else {
                // 결과가 있다면 버튼 위 텍스트뷰에 JSON 데이터들을 텍스트뷰 형태에 맞게 출력한다

                Toast.makeText(context,"다이어리가 삭제 되었습니다.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, CalenderDiaryActivity.class);
                intent.putExtra("start",date);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
            }
        }

        /* AsyncTask가 수행할 작업 내용을 정의하는 함수 */
        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];       // IP_ADDRESS에 적은 퍼블릭 IPv4 주소를 저장할 변수
//            Log.e("params[0] : ", params[0].toString());
            String postParameters = params[1];  // HttpUrlConnection 결과로 얻은 Request body에 담긴 내용들을 저장할 변수
//            Log.e("params[1] : ", params[1].toString());


            try {

                URL url = new URL(serverURL);
                Log.e("확인",serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }

}
