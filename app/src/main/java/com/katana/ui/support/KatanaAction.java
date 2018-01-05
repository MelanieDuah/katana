package com.katana.ui.support;

import android.os.Bundle;

/**
 * Created by AOwusu on 12/24/2017.
 */

public interface KatanaAction {
     void Invoke(String param, Bundle bundle);
     <T> T Invoke(String param);
}
