package com.example.carmaintance.userPage.typePage;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carmaintance.R;
import com.example.carmaintance.adapter.TypeProgramAdapter;
import com.example.carmaintance.bean.TypeProgram;
import com.example.carmaintance.userPage.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class TypeFragment extends BaseFragment implements View.OnClickListener {

    private View view;
    private EditText searchProgram;
    private TextView programSearch;
    private TextView beautyCleaning;
    private TextView routineMaintain;
    private TextView commonAccessory;
    private TextView airMaintain;
    private TextView brakeMaintain;
    private TextView waterTank;
    private TextView highMileage;
    private TextView interior;
    private RecyclerView recyclerView;
    private TypeProgramAdapter mAdapter;
    private List<TypeProgram> list = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_type, container, false);
        searchProgram = (EditText) view.findViewById(R.id.search_program_text);
        programSearch = (TextView) view.findViewById(R.id.program_search);
        beautyCleaning = (TextView) view.findViewById(R.id.beauty_cleaning);
        routineMaintain = (TextView) view.findViewById(R.id.routine_maintain);
        commonAccessory = (TextView) view.findViewById(R.id.common_accessory);
        airMaintain = (TextView) view.findViewById(R.id.air_maintenance);
        brakeMaintain = (TextView) view.findViewById(R.id.brake_maintain);
        waterTank = (TextView) view.findViewById(R.id.water_tank_maintenance);
        highMileage = (TextView) view.findViewById(R.id.high_mileage);
        interior = (TextView) view.findViewById(R.id.interior);

        recyclerView = (RecyclerView) view.findViewById(R.id.type_program_recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),3);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new TypeProgramAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);

        programSearch.setOnClickListener(this);
        beautyCleaning.setOnClickListener(this);
        routineMaintain.setOnClickListener(this);
        commonAccessory.setOnClickListener(this);
        airMaintain.setOnClickListener(this);
        brakeMaintain.setOnClickListener(this);
        waterTank.setOnClickListener(this);
        highMileage.setOnClickListener(this);
        interior.setOnClickListener(this);
        initData();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.program_search:
                Intent intent = new Intent(getContext(), SearchProgramActivity.class);
                intent.putExtra("program", searchProgram.getText().toString());
                startActivity(intent);
                break;
            case R.id.beauty_cleaning:
                Log.i("????????????", "????????????");
                initData();
                break;
            case R.id.routine_maintain:
                list.clear();
                TypeProgram program9 = new TypeProgram();
                program9.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqdzumzsoj304n04rt8q.jpg");
                program9.setProgramName("??????");
                list.add(program9);
                TypeProgram program10 = new TypeProgram();
                program10.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqe05hi7zj304m04wdfq.jpg");
                program10.setProgramName("???????????????");
                list.add(program10);
                TypeProgram program11 = new TypeProgram();
                program11.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqe0mzswpj304n04h0sm.jpg");
                program11.setProgramName("???????????????");
                list.add(program11);
                TypeProgram program12 = new TypeProgram();
                program12.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqe1wktu9j304w0523yd.jpg");
                program12.setProgramName("???????????????");
                list.add(program12);
                TypeProgram program13 = new TypeProgram();
                program13.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqe2asdvoj304p04oa9z.jpg");
                program13.setProgramName("??????????????????");
                list.add(program13);
                TypeProgram program14 = new TypeProgram();
                program14.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqe2or06zj304o04sjrf.jpg");
                program14.setProgramName("???????????????");
                list.add(program14);
                mAdapter.updateDates(list);
                break;
            case R.id.common_accessory:
                list.clear();
                TypeProgram program15 = new TypeProgram();
                program15.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqeg2i8yvj304i04p3yh.jpg");
                program15.setProgramName("?????????");
                list.add(program15);
                TypeProgram program16 = new TypeProgram();
                program16.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqeggfd0fj304q04d3yi.jpg");
                program16.setProgramName("????????????");
                list.add(program16);
                TypeProgram program17 = new TypeProgram();
                program17.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqegrwtymj304q04q0ss.jpg");
                program17.setProgramName("??????");
                list.add(program17);
                TypeProgram program18 = new TypeProgram();
                program18.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqeh3s8q2j304m04nt8q.jpg");
                program18.setProgramName("?????????");
                list.add(program18);
                TypeProgram program19 = new TypeProgram();
                program19.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqehdutzpj304x050q2y.jpg");
                program19.setProgramName("??????");
                list.add(program19);
                mAdapter.updateDates(list);
                break;
            case R.id.air_maintenance:
                list.clear();
                TypeProgram program20 = new TypeProgram();
                program20.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqekbdpq9j304q04pt8n.jpg");
                program20.setProgramName("???????????????");
                list.add(program20);
                TypeProgram program21 = new TypeProgram();
                program21.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqemk5dcgj304g04haa5.jpg");
                program21.setProgramName("???????????????");
                list.add(program21);
                TypeProgram program22 = new TypeProgram();
                program22.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqen0mg1oj304j04u3yf.jpg");
                program22.setProgramName("??????????????????");
                list.add(program22);
                TypeProgram program23 = new TypeProgram();
                program23.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqenbsdwgj304s059q2z.jpg");
                program23.setProgramName("??????????????????");
                list.add(program23);
                mAdapter.updateDates(list);
                break;
            case R.id.brake_maintain:
                list.clear();
                TypeProgram program24 = new TypeProgram();
                program24.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqermcaxij305304sq30.jpg");
                program24.setProgramName("???????????????");
                list.add(program24);
                TypeProgram program25 = new TypeProgram();
                program25.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqerytq8lj304p04raa4.jpg");
                program25.setProgramName("?????????");
                list.add(program25);
                TypeProgram program26 = new TypeProgram();
                program26.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqes79966j304q04vglk.jpg");
                program26.setProgramName("?????????");
                list.add(program26);
                TypeProgram program27 = new TypeProgram();
                program27.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqesifrcpj304v04tmx4.jpg");
                program27.setProgramName("?????????");
                list.add(program27);
                mAdapter.updateDates(list);
                break;
            case R.id.water_tank_maintenance:
                list.clear();
                TypeProgram program28 = new TypeProgram();
                program28.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqevmb28bj304g04qq2y.jpg");
                program28.setProgramName("?????????");
                list.add(program28);
                TypeProgram program29 = new TypeProgram();
                program29.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqevx72e4j304i04uwee.jpg");
                program29.setProgramName("????????????");
                list.add(program29);
                TypeProgram program30 = new TypeProgram();
                program30.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqew80x0nj304h04kjra.jpg");
                program30.setProgramName("????????????");
                list.add(program30);
                mAdapter.updateDates(list);
                break;
            case R.id.high_mileage:
                list.clear();
                TypeProgram program31 = new TypeProgram();
                program31.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqeyhjzprj304l04uaa4.jpg");
                program31.setProgramName("????????????");
                list.add(program31);
                TypeProgram program32 = new TypeProgram();
                program32.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqf14vr04j304t04it8q.jpg");
                program32.setProgramName("????????????");
                list.add(program32);
                TypeProgram program33 = new TypeProgram();
                program33.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqf1o4ji0j304k04k749.jpg");
                program33.setProgramName("???????????????");
                list.add(program33);
                TypeProgram program34 = new TypeProgram();
                program34.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqf1yqpudj304g04mwef.jpg");
                program34.setProgramName("?????????");
                list.add(program34);
                mAdapter.updateDates(list);
                break;
            case R.id.interior:
                list.clear();
                TypeProgram program35 = new TypeProgram();
                program35.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqf72od90j304g04pwef.jpg");
                program35.setProgramName("??????");
                list.add(program35);
                TypeProgram program36 = new TypeProgram();
                program36.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqf8u4nutj304b04ia9y.jpg");
                program36.setProgramName("??????");
                list.add(program36);
                TypeProgram program37 = new TypeProgram();
                program37.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqf999gxgj304804gt8m.jpg");
                program37.setProgramName("????????????");
                list.add(program37);
                TypeProgram program38 = new TypeProgram();
                program38.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqf9hzh4ej304w052jra.jpg");
                program38.setProgramName("????????????");
                list.add(program38);
                mAdapter.updateDates(list);
                TypeProgram program39 = new TypeProgram();
                program39.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqf9r3rmkj304l04t0sr.jpg");
                program39.setProgramName("?????????");
                list.add(program39);
                TypeProgram program40 = new TypeProgram();
                program40.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqfa0s9clj304m04p745.jpg");
                program40.setProgramName("????????????");
                list.add(program40);
                TypeProgram program41 = new TypeProgram();
                program41.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqfaai4qaj304j04tgli.jpg");
                program41.setProgramName("??????");
                list.add(program41);
                TypeProgram program42 = new TypeProgram();
                program42.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqfaj1ze8j304l04jdfs.jpg");
                program42.setProgramName("??????");
                list.add(program42);
                TypeProgram program43 = new TypeProgram();
                program43.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqfathv76j304m04awee.jpg");
                program43.setProgramName("??????");
                list.add(program43);
                break;
        }
    }

    private void initData() {
        list.clear();
        TypeProgram program1 = new TypeProgram();
        program1.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqd5rt3uwj304v04yaa2.jpg");
        program1.setProgramName("??????");
        list.add(program1);
        TypeProgram program2 = new TypeProgram();
        program2.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqd69hcw9j304e04dgll.jpg");
        program2.setProgramName("??????");
        list.add(program2);
        TypeProgram program3 = new TypeProgram();
        program3.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqd6hsii6j304p04r3yk.jpg");
        program3.setProgramName("??????");
        list.add(program3);
        TypeProgram program4 = new TypeProgram();
        program4.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqdhq7p6aj30550560st.jpg");
        program4.setProgramName("??????");
        list.add(program4);
        TypeProgram program5 = new TypeProgram();
        program5.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqdidd1v2j305004l0st.jpg");
        program5.setProgramName("??????");
        list.add(program5);
        TypeProgram program6 = new TypeProgram();
        program6.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqdiwxeb1j304u04paa5.jpg");
        program6.setProgramName("????????????");
        list.add(program6);
        TypeProgram program7 = new TypeProgram();
        program7.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqdjeogx4j304r04t74c.jpg");
        program7.setProgramName("????????????");
        list.add(program7);
        TypeProgram program8 = new TypeProgram();
        program8.setProgramImg("https://ww1.sinaimg.cn/large/0077HGE3ly1gqqdjy3jwxj305404nmxa.jpg");
        program8.setProgramName("????????????");
        list.add(program8);
        mAdapter.updateDates(list);
    }
}