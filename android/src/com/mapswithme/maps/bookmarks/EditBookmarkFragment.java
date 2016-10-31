package com.mapswithme.maps.bookmarks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mapswithme.maps.R;
import com.mapswithme.maps.base.BaseMwmFragment;
import com.mapswithme.maps.bookmarks.ChooseBookmarkCategoryFragment.Listener;
import com.mapswithme.maps.bookmarks.data.Bookmark;
import com.mapswithme.maps.bookmarks.data.BookmarkManager;
import com.mapswithme.maps.bookmarks.data.Icon;
import com.mapswithme.util.statistics.Statistics;

import static com.mapswithme.maps.bookmarks.EditBookmarkActivity.EXTRA_BOOKMARK_ID;
import static com.mapswithme.maps.bookmarks.EditBookmarkActivity.EXTRA_CATEGORY_ID;

public class EditBookmarkFragment extends BaseMwmFragment implements View.OnClickListener, Listener
{
  private EditText mEtDescription;
  private EditText mEtName;
  private TextView mTvBookmarkGroup;
  private ImageView mIvColor;
  private Bookmark mBookmark;

  public EditBookmarkFragment() {}

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    return inflater.inflate(R.layout.edit_bookmark_common, container, false);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState)
  {
    super.onViewCreated(view, savedInstanceState);

    final Bundle args = getArguments();
    int categoryId = args.getInt(EXTRA_CATEGORY_ID);
    int bookmarkId = args.getInt(EXTRA_BOOKMARK_ID);
    mBookmark = BookmarkManager.INSTANCE.getBookmark(categoryId, bookmarkId);

    mEtName = (EditText) view.findViewById(R.id.et__bookmark_name);
    mEtDescription = (EditText) view.findViewById(R.id.et__description);
    mTvBookmarkGroup = (TextView) view.findViewById(R.id.tv__bookmark_set);
    mTvBookmarkGroup.setOnClickListener(this);
    mIvColor = (ImageView) view.findViewById(R.id.iv__bookmark_color);
    mIvColor.setOnClickListener(this);
    refreshBookmark();
  }

  public void saveBookmark()
  {
    mBookmark.setParams(mEtName.getText().toString(), null, mEtDescription.getText().toString());
  }

  @Override
  public void onClick(View v)
  {
    switch (v.getId())
    {
    case R.id.iv__bookmark_color:
      selectBookmarkColor();
      break;
    case R.id.tv__bookmark_set:
      selectBookmarkSet();
      break;
    }
  }

  private void selectBookmarkSet()
  {
    final Bundle args = new Bundle();
    args.putInt(ChooseBookmarkCategoryFragment.CATEGORY_ID, mBookmark.getCategoryId());
    args.putInt(ChooseBookmarkCategoryFragment.BOOKMARK_ID, mBookmark.getBookmarkId());
    final ChooseBookmarkCategoryFragment fragment = (ChooseBookmarkCategoryFragment) Fragment.instantiate(getActivity(), ChooseBookmarkCategoryFragment.class.getName(), args);
    fragment.show(getChildFragmentManager(), null);
  }

  private void selectBookmarkColor()
  {
    final Bundle args = new Bundle();
    args.putString(BookmarkColorDialogFragment.ICON_TYPE, mBookmark.getIcon().getType());
    final BookmarkColorDialogFragment dialogFragment = (BookmarkColorDialogFragment) BookmarkColorDialogFragment.
        instantiate(getActivity(), BookmarkColorDialogFragment.class.getName(), args);

    dialogFragment.setOnColorSetListener(new BookmarkColorDialogFragment.OnBookmarkColorChangeListener()
    {
      @Override
      public void onBookmarkColorSet(int colorPos)
      {
        final Icon newIcon = BookmarkManager.ICONS.get(colorPos);
        final String from = mBookmark.getIcon().getName();
        final String to = newIcon.getName();
        if (TextUtils.equals(from, to))
          return;

        Statistics.INSTANCE.trackColorChanged(from, to);
        mBookmark.setParams(mBookmark.getTitle(), newIcon, mBookmark.getBookmarkDescription());
        mBookmark = BookmarkManager.INSTANCE.getBookmark(mBookmark.getCategoryId(), mBookmark.getBookmarkId());
        refreshColorMarker();
      }
    });

    dialogFragment.show(getActivity().getSupportFragmentManager(), null);
  }

  private void refreshColorMarker()
  {
    mIvColor.setImageResource(mBookmark.getIcon().getSelectedResId());
  }

  private void refreshBookmark()
  {
    if (TextUtils.isEmpty(mEtName.getText()))
      mEtName.setText(mBookmark.getTitle());

    if (TextUtils.isEmpty(mEtDescription.getText()))
      mEtDescription.setText(mBookmark.getBookmarkDescription());
    mTvBookmarkGroup.setText(mBookmark.getCategoryName());
    refreshColorMarker();
  }

  @Override
  public void onCategoryChanged(int bookmarkId, int newCategoryId)
  {
    mBookmark = BookmarkManager.INSTANCE.getBookmark(newCategoryId, bookmarkId);
    refreshBookmark();
  }
}
