package de.ultitech.matchingshape;

import android.preference.ListPreference;
import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by davidgreiner on 3/2/15.
 */

public class DynamicListPreference extends ListPreference {
    public interface DynamicListPreferenceOnClickListener {
        public void onClick(DynamicListPreference preference);
    }

    private DynamicListPreferenceOnClickListener mOnClicListener;

    public DynamicListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onClick(){
        if (mOnClicListener != null)
            mOnClicListener.onClick(this);
        super.onClick();
    }

    public void setOnClickListener(DynamicListPreferenceOnClickListener l) {
        mOnClicListener = l;
    }

}