package np.com.naxa.safercities.profile.wardprofile.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import np.com.naxa.safercities.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WardMapFragment extends Fragment {


    public WardMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ward_profile, container, false);
    }

}
