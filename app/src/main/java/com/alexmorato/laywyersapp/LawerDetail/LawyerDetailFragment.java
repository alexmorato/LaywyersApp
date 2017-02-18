package com.alexmorato.laywyersapp.LawerDetail;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexmorato.laywyersapp.Helpers.ToastHelper;
import com.alexmorato.laywyersapp.LawerAddUpdate.AddEditLawyerActivity;
import com.alexmorato.laywyersapp.R;
import com.alexmorato.laywyersapp.data.Lawyer;
import com.alexmorato.laywyersapp.data.LawyersDbHelper;
import com.alexmorato.laywyersapp.lawyers.LawyersActivity;
import com.alexmorato.laywyersapp.lawyers.LawyersFragment;
import com.bumptech.glide.Glide;

/**
 * Vista para el detalle del abogado
 */
public class LawyerDetailFragment extends Fragment {
    private static final String ARG_LAWYER_ID = "arg_lawyer_id";
    private static final int REQUEST_UPDATE_DELETE_LAWYER = 10;

    private String mLawyerId;

    private CollapsingToolbarLayout mCollapsingView;
    private ImageView mAvatar;
    private TextView mPhoneNumber;
    private TextView mSpecialty;
    private TextView mBio;

    private LawyersDbHelper mLawyersDbHelper;


    public LawyerDetailFragment() {
        // Required empty public constructor
    }

    public static LawyerDetailFragment newInstance(String lawyerId) {
        LawyerDetailFragment fragment = new LawyerDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LAWYER_ID, lawyerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mLawyerId = getArguments().getString(ARG_LAWYER_ID);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lawyer_detail, container, false);
        mCollapsingView = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);
        mAvatar = (ImageView) getActivity().findViewById(R.id.iv_avatar);
        mPhoneNumber = (TextView) root.findViewById(R.id.tv_phone_number);
        mSpecialty = (TextView) root.findViewById(R.id.tv_specialty);
        mBio = (TextView) root.findViewById(R.id.tv_bio);

        mLawyersDbHelper = new LawyersDbHelper(getActivity());

        loadLawyer();

        return root;
    }

    private void loadLawyer() {
        new GetLawyerByIdTask().execute();
    }

    private void showLawyer(Lawyer lawyer) {
        int resourceId;
        if(lawyer.getAvatarUri().contains("man"))
            resourceId = R.drawable.man;
        else
            resourceId = R.drawable.girl;


        mCollapsingView.setTitle(lawyer.getName());
        Glide.with(this)
                .load(resourceId)
                .centerCrop()
                .into(mAvatar);
        mPhoneNumber.setText(lawyer.getPhoneNumber());
        mSpecialty.setText(lawyer.getSpecialty());
        mBio.setText(lawyer.getBio());
    }

    private void showLoadError() {
        ToastHelper.ShowMessage(getContext(), "Error al cargar información");
    }

    private class GetLawyerByIdTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return mLawyersDbHelper.getLawyerById(mLawyerId);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.moveToLast()) {
                showLawyer(new Lawyer(cursor));
            } else {
                showLoadError();
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            switch (requestCode) {
                case AddEditLawyerActivity.REQUEST_ADD_LAWYER:
                    ToastHelper.ShowMessage(getContext(), "Guardado OK");
                    loadLawyers();
                    break;
                case AddEditLawyerActivity.REQUEST_UPDATE_DELETE_LAWYER:
                    loadLawyers();
                    break;
            }
        }
    }

    private void loadLawyers() {
        Intent intent = new Intent(getActivity(), LawyersActivity.class);
        startActivityForResult(intent, LawyersFragment.REQUEST_UPDATE_DELETE_LAWYER);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                showEditScreen();
                break;
            case R.id.action_delete:
                new DeleteLawyerTask().execute();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DeleteLawyerTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            return mLawyersDbHelper.deleteLawyer(mLawyerId);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            showLawyersScreen(integer > 0);
        }
    }

    private void showLawyersScreen(boolean requery) {
        if (!requery) {
            ToastHelper.ShowMessage(getContext(), "Error al borrar abogado");
        }
        getActivity().setResult(requery ? Activity.RESULT_OK : Activity.RESULT_CANCELED);
        getActivity().finish();
    }

    private void showEditScreen() {
        Intent intent = new Intent(getActivity(), AddEditLawyerActivity.class);
        intent.putExtra(LawyersActivity.EXTRA_LAWYER_ID, mLawyerId);
        startActivityForResult(intent, LawyersFragment.REQUEST_UPDATE_DELETE_LAWYER);
    }
}
