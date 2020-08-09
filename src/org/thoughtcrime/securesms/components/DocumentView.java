package org.thoughtcrime.securesms.components;


import android.content.Context;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import org.thoughtcrime.securesms.R;
import org.thoughtcrime.securesms.mms.DocumentSlide;
import org.thoughtcrime.securesms.mms.SlideClickListener;
import org.thoughtcrime.securesms.util.Util;
import org.thoughtcrime.securesms.util.ViewUtil;
import org.thoughtcrime.securesms.util.guava.Optional;

import java.util.zip.GZIPInputStream;

public class DocumentView extends FrameLayout {

  private static final String TAG = DocumentView.class.getSimpleName();

  private final @NonNull View            container;
  private final @NonNull TextView        fileName;
  private final @NonNull TextView        fileSize;
  private final @NonNull LottieAnimationView lottie;

  private @Nullable SlideClickListener viewListener;
  private @Nullable DocumentSlide      documentSlide;

  public DocumentView(@NonNull Context context) {
    this(context, null);
  }

  public DocumentView(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public DocumentView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    inflate(context, R.layout.document_view, this);

    this.container        = findViewById(R.id.document_container);
    this.fileName         = findViewById(R.id.file_name);
    this.fileSize         = findViewById(R.id.file_size);
    this.lottie           = findViewById(R.id.doc_animation);
  }

  public void setDocumentClickListener(@Nullable SlideClickListener listener) {
    this.viewListener = listener;
  }

  public void setDocument(final @NonNull DocumentSlide documentSlide)
  {
      String filename = documentSlide.getFileName().or("");
      if ((filename.endsWith(".tgs"))) {
	  container.setVisibility(GONE);
	  lottie.setVisibility(VISIBLE);
	  lottie.setOnFocusChangeListener((v, hasFocus) -> {
		  if (v instanceof LottieAnimationView) {
		      ((LottieAnimationView) v).resumeAnimation();
		  }
	      });
	  ViewUtil.updateLayoutParams(lottie, ViewGroup.LayoutParams.WRAP_CONTENT, 300);
	  try {
	      lottie.setAnimation(new GZIPInputStream(getContext().getContentResolver().openInputStream(documentSlide.getUri())), filename);
	  } catch (Exception e) {
	      e.printStackTrace();
	  }
	  lottie.setOnClickListener(v -> {
		  if (v instanceof LottieAnimationView) {
		      ((LottieAnimationView) v).setRepeatCount(5);
		      ((LottieAnimationView) v).resumeAnimation();
		  }
	      });
	  lottie.clearAnimation();
	  lottie.animate();
      } else {
	  container.setVisibility(VISIBLE);
	  lottie.setVisibility(GONE);

	  this.documentSlide = documentSlide;

	  this.fileName.setText(documentSlide.getFileName().or(getContext().getString(R.string.unknown)));

	  String fileSize = Util.getPrettyFileSize(documentSlide.getFileSize())
	      + " " + getFileType(documentSlide.getFileName()).toUpperCase();
	  this.fileSize.setText(fileSize);

	  this.setOnClickListener(new OpenClickedListener(documentSlide));
      }
  }

  @Override
  public void setFocusable(boolean focusable) {
    super.setFocusable(focusable);
  }

  @Override
  public void setClickable(boolean clickable) {
    super.setClickable(clickable);
  }

  @Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
  }

  private @NonNull String getFileType(Optional<String> fileName) {
    if (!fileName.isPresent()) return "";

    String[] parts = fileName.get().split("\\.");

    if (parts.length < 2) {
      return "";
    }

    String suffix = parts[parts.length - 1];

    if (suffix.length() <= 4) {
      return suffix;
    }

    return "";
  }

  private class OpenClickedListener implements View.OnClickListener {
    private final @NonNull DocumentSlide slide;

    private OpenClickedListener(@NonNull DocumentSlide slide) {
      this.slide = slide;
    }

    @Override
    public void onClick(View v) {
      if (viewListener != null) {
        viewListener.onClick(v, slide);
      }
    }
  }

}
