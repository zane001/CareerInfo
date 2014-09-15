package com.zane001.careerinfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main extends Activity {

    String url = "http://job.bupt.edu.cn/career/careerTalkDetail";
    List<String> infos;
    String date[];
    String company[];
    String time[];
    String place[];
    private ListView lv_joblists;
    List<Map<String, ?>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        lv_joblists = (ListView) findViewById(R.id.lv_joblists);
        new LoadHtml().execute();

    }

    class LoadHtml extends AsyncTask<String, String, List<String>> {

        ProgressDialog progressDialog;
        int company_index = 0;
        int date_index = 0;
        int time_index = 0;
        int place_index = 0;

        @Override
        protected List<String> doInBackground(String... strings) {
            try {
                Document doc = Jsoup.connect(url).get();
                Elements tds = doc.getElementsByTag("td");
                int length = tds.size() / 5;
                date = new String[length];
                company = new String[length];
                time = new String[length];
                place = new String[length];
                for (int i = 0; i < tds.size(); i++) {
                    if (i % 5 == 0) {
                        date[date_index] = tds.get(i).text();
                        date_index++;
                    } else if (i % 5 == 1) {

                    } else if (i % 5 == 2) {
                        company[company_index] = tds.get(i).text();
                        company_index++;
                    } else if (i % 5 == 3) {
                        time[time_index] = tds.get(i).text();
                        time_index++;
                    } else if (i % 5 == 4) {
                        place[place_index] = tds.get(i).text();
                        place_index++;
                    }
                }
                infos = new ArrayList<String>();
                for (int j = 0; j < length; j++) {
//                    infos += date[j] + ":" + company[j] + ":" + time[j] + ":" + place[j] + "\n";
                    infos.add(date[j] + "@" + company[j] + "@" + time[j] + "@" + place[j] + "\n");
                }
                list = new ArrayList<Map<String, ?>>();
                for (int i = 0; i < infos.size(); i++) {
                    Map<String, String> m = new HashMap<String, String>();
                    String[] temp = infos.get(i).split("@");
                    m.put("date", temp[0]);
                    m.put("company", temp[1]);
                    m.put("time", temp[2]);
                    m.put("place", temp[3]);
                    list.add(m);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return infos;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Main.this);
            progressDialog.setMessage("正在加载数据。。。");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
            progressDialog.dismiss();
            if (list.size() > 0) {
                SimpleAdapter adapter = new SimpleAdapter(Main.this, list, R.layout.item,
                        new String[]{"date", "company", "time", "place"},
                        new int[]{R.id.tv_date, R.id.tv_company, R.id.tv_time, R.id.tv_place});
                lv_joblists.setAdapter(adapter);
            }
        }

    }

    /**
     * 适配器对象
     *//*
    private class JobAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return infos.size();
        }

        @Override
        public Object getItem(int position) {
                return infos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

                View view;
                ViewHolder holder;
                //复用历史缓存
                if (convertView == null) {
                    view = View.inflate(getApplicationContext(), R.layout.item, null);
                    holder = new ViewHolder();
                    holder.tv_date = (TextView) view.findViewById(R.id.tv_date);
                    holder.tv_company = (TextView) view.findViewById(R.id.tv_company);
                    holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
                    holder.tv_place = (TextView) view.findViewById(R.id.tv_place);
                    view.setTag(holder);
                } else {
                    view = convertView;
                    holder = (ViewHolder) view.getTag();
                }
                //适配数据
                String temp = infos.get(position);
                String[] temp1 = temp.split(":");
                String date_text = temp1[0];
                String company_text = temp1[1];
                String time_text = temp1[2];
                String place_text = temp1[3];
                holder.tv_date.setText(date_text);
                holder.tv_company.setText(company_text);
                holder.tv_time.setText(time_text);
                holder.tv_place.setText(place_text);
                return view;
            }
        }

    //用static修饰保证只存在一份
    private static class ViewHolder {
        TextView tv_date;
        TextView tv_company;
        TextView tv_time;
        TextView tv_place;
    }*/
}
