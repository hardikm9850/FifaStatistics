package com.example.kevin.fifastatistics.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.FragmentSeriesBinding;
import com.example.kevin.fifastatistics.managers.RetrofitErrorManager;
import com.example.kevin.fifastatistics.models.databasemodels.match.Series;
import com.example.kevin.fifastatistics.models.databasemodels.match.SeriesProjection;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.viewmodels.fragment.SeriesFragmentViewModel;

public class SeriesFragment extends FifaBaseFragment implements SeriesFragmentViewModel.OnSeriesLoadedListener {

    private Series mSeries;
    private SeriesProjection mSeriesProjection;
    private String mSeriesId;
    private User mUser;
    private SeriesFragmentViewModel mViewModel;
    private FragmentSeriesBinding mBinding;
    private OnSeriesLoadSuccessListener mSeriesListener;

    public static SeriesFragment newInstance(SeriesProjection seriesProjection, User user) {
        SeriesFragment f = new SeriesFragment();
        Bundle args = new Bundle();
        args.putSerializable(USER, user);
        args.putSerializable(SERIES_PROJECTION, seriesProjection);
        f.setArguments(args);
        return f;
    }

    public static SeriesFragment newInstance(String seriesId, User user) {
        SeriesFragment f = new SeriesFragment();
        Bundle args = new Bundle();
        args.putSerializable(USER, user);
        args.putString(EVENT_ID, seriesId);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSeriesLoadSuccessListener) {
            mSeriesListener = (OnSeriesLoadSuccessListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mSeriesListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restoreInstance(savedInstanceState);
    }

    private void restoreInstance(Bundle savedInstanceState) {
        Bundle b = savedInstanceState == null ? getArguments() : savedInstanceState;
        mUser = (User) b.getSerializable(USER);
        mSeries = (Series) b.getSerializable(SERIES);
        mSeriesProjection = (SeriesProjection) b.getSerializable(SERIES_PROJECTION);
        mSeriesId = b.getString(EVENT_ID);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(USER, mUser);
        outState.putSerializable(SERIES, mSeries);
        outState.putSerializable(SERIES_PROJECTION, mSeriesProjection);
        outState.putString(EVENT_ID, mSeriesId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_series, container, false);
        mViewModel = new SeriesFragmentViewModel(this, mSeriesProjection, mUser, mSeriesId, this);
        mBinding.setViewModel(mViewModel);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel.setSeries(mSeries);
        mViewModel.loadSeries();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mViewModel != null) {
            mViewModel.destroy();
        }
    }

    @Override
    public void onSeriesLoaded(Series series) {
        mSeries = series;
        if (mSeriesListener != null) {
            mSeriesListener.onSeriesLoad(series);
        }
    }

    @Override
    public void onSeriesLoadFailed(Throwable t) {
        RetrofitErrorManager.showToastForError(t, getActivity());
        getActivity().finish();
    }

    public interface OnSeriesLoadSuccessListener {
        void onSeriesLoad(Series series);
    }
}
