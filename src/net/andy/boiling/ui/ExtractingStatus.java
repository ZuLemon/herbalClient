package net.andy.boiling.ui;

import android.app.Activity;
import android.os.Bundle;
import org.xutils.x;

/**
 *
 * Created by Guang on 2016/8/19.
 */

public class ExtractingStatus extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

    }
}
