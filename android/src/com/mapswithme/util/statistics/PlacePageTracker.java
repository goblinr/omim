package com.mapswithme.util.statistics;

import android.app.Activity;
import android.graphics.Rect;
import android.support.annotation.NonNull;
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

  private boolean mTracked;

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
    mTracked = false;
  }

  private void trackTaxiVisibility()
  {
    if (!mTracked && isViewOnScreen(mTaxi))
    {
      Statistics.INSTANCE.trackTaxiShow();
      mTracked = true;
    }
  }

  private boolean isViewOnScreen(@NonNull View view) {

    if (mPlacePageView.getVisibility() == INVISIBLE)
      return false;

    Rect localRect = new Rect();
    Rect globalRect = new Rect();
    view.getLocalVisibleRect(localRect);
    view.getGlobalVisibleRect(globalRect);
    return localRect.bottom >= view.getHeight() && localRect.top == 0
           && globalRect.bottom <= mPlacePageView.getBottom() - mBottomPadding;
  }
}
