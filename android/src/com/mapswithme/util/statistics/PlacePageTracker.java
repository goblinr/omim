package com.mapswithme.util.statistics;

import android.app.Activity;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.mapswithme.maps.R;
import com.mapswithme.maps.widget.placepage.PlacePageView;

import static android.view.View.INVISIBLE;

public class PlacePageTracker
{
  private final int mBottomPadding;
  @NonNull
  private final PlacePageView mPlacePageView;
  @NonNull
  private final View mTaxi;

  public PlacePageTracker(@NonNull PlacePageView placePageView)
  {
    mPlacePageView = placePageView;
    mTaxi = mPlacePageView.findViewById(R.id.ll__place_page_taxi);
    Activity activity = (Activity) placePageView.getContext();
    mBottomPadding = activity.getResources().getDimensionPixelOffset(R.dimen.place_page_buttons_height);
  }

  public void onMove()
  {
    trackTaxiVisibility();
  }

  public void onHide()
  {
    Log.i("XXX", "onHide()");
  }

  private void trackTaxiVisibility()
  {
    Log.i("XXX", "isViewVIsible taxi = " + isViewOnScreen(mTaxi));

    //TODO: send statistics
  }

  private boolean isViewOnScreen(@NonNull View view) {

    if (mPlacePageView.getVisibility() == INVISIBLE)
      return false;

    Rect rect = new Rect();
    view.getGlobalVisibleRect(rect);

    Log.i("XXX", "bottom = " + rect.bottom + " pp bottom = " + mPlacePageView.getBottom() + " padding = " + mBottomPadding);
    return rect.bottom <= mPlacePageView.getBottom() - mBottomPadding;
  }

}
