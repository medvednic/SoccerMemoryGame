package itworks.eddy.soccermemorygame.Views;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class ScoresFragment extends Fragment {


    @Bind(R.id.scoresRecyclerView)
    RecyclerView scoresRecyclerView;
    ScoresAdapter scoresAdapter;
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
        //getScores();
        return view;
    }

    private void getScores(final int level) {
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
                    List<User> data = response.body().getUser();
                    scoresAdapter = new ScoresAdapter(data, level);
                    scoresRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    scoresRecyclerView.setAdapter(scoresAdapter);
                } else {
                    if (response.code() == 400) { //failed to update score
                        Log.d("failed to get scores", " 400");
                    } else { //server returned error
                        String errMsg = response.raw().message();
                        Log.d("Error", errMsg);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLvl1:
                getScores(1);
                break;
            case R.id.btnLvl2:
                getScores(2);
                break;
            case R.id.btnLvl3:
                getScores(3);
                break;
        }
    }
}
