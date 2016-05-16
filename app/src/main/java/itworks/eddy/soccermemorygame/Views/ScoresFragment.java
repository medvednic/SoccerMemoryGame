package itworks.eddy.soccermemorygame.Views;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import itworks.eddy.soccermemorygame.Models.User;
import itworks.eddy.soccermemorygame.Models.UsersList;
import itworks.eddy.soccermemorygame.R;
import itworks.eddy.soccermemorygame.RESTaccess.apiServices;
import itworks.eddy.soccermemorygame.ScoresAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/** ScoresFragment - user scores of all three difficulty levels are displayed
 *  in a RecyclerView, one and only one server call will be performed for data fetching of each level.
 *
 */
public class ScoresFragment extends Fragment {

    @Bind(R.id.scoresRecyclerView)
    RecyclerView scoresRecyclerView;
    ScoresAdapter scoresAdapter;
    List<User> data0;
    List<User> data1;
    List<User> data2;
    @Bind(R.id.btnLvl1)
    Button btnLvl1;
    @Bind(R.id.btnLvl2)
    Button btnLvl2;
    @Bind(R.id.btnLvl3)
    Button btnLvl3;

    public ScoresFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scores, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void getScores(final int level) { //get scores from database
        final String BASE_URL = getContext().getString(R.string.api_server_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiServices api = retrofit.create(apiServices.class);
        Call<UsersList> getScores = api.getScores(level);
        getScores.enqueue(new Callback<UsersList>() {
            @Override
            public void onResponse(Call<UsersList> call, Response<UsersList> response) {
                if (response.isSuccessful()) { //if score was updated
                    //corresponding list of users (scores) will be initialized according to desired level
                    if (level == 1){
                        data0 = response.body().getUsers();
                        initRecyclerView(data0, level);
                    }else if (level == 2){
                        data1 = response.body().getUsers();
                        initRecyclerView(data1, level);
                    }else {
                        data2 = response.body().getUsers();
                        initRecyclerView(data2, level);
                    }
                    enableButtons();
                } else {
                    if (response.code() == 400) { //failed to update score
                        Toast.makeText(getContext(), "Failed to ge scores", Toast.LENGTH_SHORT).show();
                    } else { //server returned unexpected error
                        Toast.makeText(getContext(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UsersList> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btnLvl1, R.id.btnLvl2, R.id.btnLvl3})
    public void onClick(View view) { //if data was previously fetched, no server call will be made
        switch (view.getId()) {
            case R.id.btnLvl1:
                if (data0 == null){
                    disableButtons();
                    getScores(1);
                }else {
                    initRecyclerView(data0, 1);
                }
                break;
            case R.id.btnLvl2:
                if (data1 == null){
                    disableButtons();
                    getScores(2);
                }else {
                    initRecyclerView(data1, 2);
                }
                break;
            case R.id.btnLvl3:
                if (data2 == null){
                    disableButtons();
                    getScores(3);
                }else {
                    initRecyclerView(data2, 3);
                }
                break;
        }
    }

    private void disableButtons() {
        btnLvl1.setEnabled(false);
        btnLvl2.setEnabled(false);
        btnLvl3.setEnabled(false);
    }

    private void enableButtons() {
        btnLvl1.setEnabled(true);
        btnLvl2.setEnabled(true);
        btnLvl3.setEnabled(true);
    }

    public void initRecyclerView(List<User> data, int level){ //initialize adapter and RecyclerView
        scoresAdapter = new ScoresAdapter(data, level);
        scoresRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        scoresRecyclerView.setAdapter(scoresAdapter);
    }
}
