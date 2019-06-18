package r19033.foi.hr.infoapp.utils;

import android.app.ProgressDialog;
import android.content.Context;

import r19033.foi.hr.infoapp.R;

public class LoadProgress {

  private ProgressDialog dialog;
  private Context context;

  public LoadProgress(Context context) {
    this.context = context;
  }

  public void showDialog() {
    dialog = new ProgressDialog(context, R.style.MyTheme);
    dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
    dialog.setCancelable(false);
    dialog.show();
  }

  public void dissmissDialog() {
    if (dialog != null) {
      if (dialog.isShowing()) {
        dialog.dismiss();
      }
    }
  }
}
