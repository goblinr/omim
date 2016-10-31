package com.mapswithme.maps.bookmarks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.mapswithme.maps.R;
import com.mapswithme.maps.base.BaseToolbarActivity;

public class EditBookmarkActivity extends BaseToolbarActivity
{
  public static final String EXTRA_CATEGORY_ID = "CategoryId";
  public static final String EXTRA_BOOKMARK_ID = "BookmarkId";

  public static void editBookmark(@NonNull Context context, int categoryId, int bookmarkId)
  {
    Intent intent = new Intent(context, EditBookmarkActivity.class);
    intent.putExtra(EXTRA_CATEGORY_ID, categoryId);
    intent.putExtra(EXTRA_BOOKMARK_ID, bookmarkId);
    context.startActivity(intent);
  }

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    final EditBookmarkFragment fragment = (EditBookmarkFragment) getSupportFragmentManager()
        .findFragmentByTag(getFragmentClass().getName());
    final TextView textView = (TextView) findViewById(R.id.tv__save);
    textView.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        if (fragment != null)
          fragment.saveBookmark();

        finish();
      }
    });
  }

  @Override
  protected int getContentLayoutResId()
  {
    return R.layout.activity_edit_bookmark;
  }

  @Override
  protected Class<? extends Fragment> getFragmentClass()
  {
    return EditBookmarkFragment.class;
  }

  @Override
  protected int getToolbarTitle()
  {
    return R.string.description;
  }
}
