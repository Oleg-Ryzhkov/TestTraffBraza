package com.example.testtraffbraza

import com.appsflyer.AppsFlyerLib

class AFApplication : MainActivity() {
     fun AppsFlyer() {
         AppsFlyerLib.getInstance().init("$appssflay", null, this)
         AppsFlyerLib.getInstance().start(this)
    }

}