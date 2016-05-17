package itworks.eddy.soccermemorygame.Views;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import itworks.eddy.soccermemorygame.R;

/**
 * AboutFragment - credits and about
 */
public class AboutFragment extends Fragment {


    @Bind(R.id.tvAuthor)
    TextView tvAuthor;
    @Bind(R.id.tvIcons)
    TextView tvIcons;
    @Bind(R.id.TvMuisc)
    TextView TvMuisc;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tvAuthor, R.id.tvIcons, R.id.TvMuisc})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tvAuthor:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/medvednic/SoccerMemoryGame"));
                getContext().startActivity(intent);
                break;
            case R.id.tvIcons:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.flaticon.com/"));
                getContext().startActivity(intent);
                break;
            case R.id.TvMuisc:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://soundcloud.com/wayne-kinos"));
                getContext().startActivity(intent);
                break;
        }
    }
}
