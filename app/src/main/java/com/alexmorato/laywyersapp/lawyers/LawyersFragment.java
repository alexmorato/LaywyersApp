package com.alexmorato.laywyersapp.lawyers;


import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alexmorato.laywyersapp.R;
import com.alexmorato.laywyersapp.data.LawyersCursorAdapter;
import com.alexmorato.laywyersapp.data.LawyersDbHelper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LawyersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LawyersFragment extends Fragment {

    private LawyersDbHelper mLawyersDbHelper;

    private ListView mLawyersList;
    private LawyersCursorAdapter mLawyersAdapter;
    private FloatingActionButton mAddButton;

    public LawyersFragment() {
        // Required empty public constructor
    }

    public static LawyersFragment newInstance() {
        return new LawyersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lawyers, container, false);

        // Referencias UI
        mLawyersList = (ListView) root.findViewById(R.id.lawyers_list);
        mLawyersAdapter = new LawyersCursorAdapter(getActivity(), null);
        mAddButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        // Setup
        mLawyersList.setAdapter(mLawyersAdapter);

        // Instancia de helper
        mLawyersDbHelper = new LawyersDbHelper(getActivity());

        // Carga de datos
        loadLawyers();

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    private void loadLawyers() {
        new LawyersLoadTask().execute();
    }

    private class LawyersLoadTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return mLawyersDbHelper.getAllLawyers();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {
                mLawyersAdapter.swapCursor(cursor);
            } else {
                // Mostrar empty state
            }
        }
    }
}
