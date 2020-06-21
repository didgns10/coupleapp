package com.example.coupleapp.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coupleapp.Activity.DateCourse.DatePlaceReviewActivity;
import com.example.coupleapp.Activity.DateCourse.DateReviewData;
import com.example.coupleapp.Activity.DateCourse.DateReviewUpdateActivity;
import com.example.coupleapp.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DateReviewAdapter extends RecyclerView.Adapter<DateReviewAdapter.ViewHolder> {

    private ArrayList<DateReviewData> mList;
    private Context context;

    private static String IP_ADDRESS="13.125.232.78";  // 퍼블릭 IPv4 주소
    private static String TAG = "스토리";          // 로그에 사용할 태그

    private String contentid,contenttypeid,title;

    public DateReviewAdapter(ArrayList<DateReviewData> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itme_review,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String name = "팀노바";

        contentid = mList.get(position).getContentid();
        contenttypeid = mList.get(position).getContenttypeid();
        title = mList.get(position).getTitle();

        holder.tv_name.setText(mList.get(position).getName());
        holder.tv_date.setText(mList.get(position).getDatef());
        holder.tv_review.setText(mList.get(position).getReview());

        Glide.with(context).load(mList.get(position).getProfile()).into(holder.cimgv_profile);

        if((mList.get(position).getReview_img()).equals("1")){
            holder.imgv_review.setVisibility(View.GONE);
        }else{
            holder.imgv_review.setVisibility(View.VISIBLE);
            Glide.with(context).load(mList.get(position).getReview_img()).into(holder.imgv_review);
        }

        float rate =(mList.get(position).getRate());
        holder.ratingBar.setNumStars(5);
        holder.ratingBar.setRating(rate);

        if((mList.get(position).getName()).equals(name)){
            holder.constraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("옵션을 선택하세요.");
                    builder.setMessage("리뷰를 수정 또는 삭제 하시겠습니까? ");
                    builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Log.e("리뷰삭제",mList.get(position).getReview_idx()+"");
                            // 그 다음 AsyncTask 객체를 만들어 execute()한다
                            GetData task = new GetData();

                            // execute() 사용 시 DB의 값을 JSON 형태로 가져오는 코드가 적힌 php 파일의 경로를 적어
                            // AsyncTask로 값들을 JSON 형태로 가져올 수 있게 한다
                            task.execute( "http://" + IP_ADDRESS + "/review_delete.php?review_idx="+mList.get(position).getReview_idx(), "");

                        }
                    });
                    builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("수정", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(context, DateReviewUpdateActivity.class);
                            intent.putExtra("rate",holder.ratingBar.getRating()+"");
                            intent.putExtra("review",holder.tv_review.getText());
                            intent.putExtra("review_img",mList.get(position).getReview_img());
                            intent.putExtra("contentid",mList.get(position).getContentid());
                            intent.putExtra("contenttypeid",mList.get(position).getContenttypeid());
                            intent.putExtra("review_idx",mList.get(position).getReview_idx());
                            context.startActivity(intent);

                        }
                    });
                    builder.create().show();


                    // 리턴값이 있다
                    // 이메서드에서 이벤트에대한 처리를 끝냈음
                    //    그래서 다른데서는 처리할 필요없음 true
                    // 여기서 이벤트 처리를 못했을 경우는 false

                    return true;
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_name,tv_date,tv_review;
        public ImageView imgv_review;
        public CircleImageView cimgv_profile;
        public RatingBar ratingBar;
        public ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_review = itemView.findViewById(R.id.rv_review);
            imgv_review = itemView.findViewById(R.id.imgv_review);
            cimgv_profile = itemView.findViewById(R.id.cimgv_profile);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            constraintLayout = itemView.findViewById(R.id.constraintlayout);

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

                Toast.makeText(context,"리뷰가 삭제 되었습니다.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, DatePlaceReviewActivity.class);
                intent.putExtra("contentid",contentid);
                intent.putExtra("contenttypeid",contenttypeid);
                intent.putExtra("title",title);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
