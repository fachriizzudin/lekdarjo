package com.lazuardifachri.bps.lekdarjo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void formatUrlFromBps() {
        String documentUrl = "https://sidoarjokab.bps.go.id/publication/download.html?nrbvfeve=ZTNlMTBkODE5ZTBiYmZlZTUzNTNkYmJk&xzmn=aHR0cHM6Ly9zaWRvYXJqb2thYi5icHMuZ28uaWQvcHVibGljYXRpb24vMjAyMS8wMi8yNi9lM2UxMGQ4MTllMGJiZmVlNTM1M2RiYmQva2FidXBhdGVuLXNpZG9hcmpvLWRhbGFtLWFuZ2thLTIwMjEuaHRtbA%3D%3D&twoadfnoarfeauf=MjAyMS0wNC0xNiAyMTo1MTo1NA%3D%3D";
        String brs = "https://sidoarjokab.bps.go.id/pressrelease/download.html?nrbvfeve=MjU%3D&sdfs=ldjfdifsdjkfahi&twoadfnoarfeauf=MjAyMS0wNC0yNSAxMToxNzoxMg%3D%3D";
        String info = "https://sidoarjokab.bps.go.id/galery/download.html?asdf=MTQ%3D&qwer=ldjfdifsdjkfahi&zxcv=MjAyMS0wNC0yNSAxMToxOToyNg%3D%3D";
        String indicator = "https://sidoarjokab.bps.go.id/statictable/download.html?nrbvfeve=MzE%3D&sdfs=ldjfdifsdjkfahi&zxcv=L2JhY2tlbmQ%3D&xzmn=aHR0cHM6Ly9zaWRvYXJqb2thYi5icHMuZ28uaWQvc3RhdGljdGFibGUvMjAxNi8wMS8yMC8zMS9sZXRhay0tdGluZ2dpLWRhbi1sdWFzLXdpbGF5YWgtbWVudXJ1dC1rZWNhbWF0YW4uaHRtbA%3D%3D&twoadfnoarfeauf=MjAyMS0wNC0yNSAxMToxOTo1MA%3D%3D";

        String update = "https://sidoarjokab.bps.go.id/galery/download.html?asdf=MTQ%3D&qwer=ldjfdifsdjkfahi&zxcv=";

        String newUrl = Utils.formatUrlFromBps(documentUrl);
        String newBrs = Utils.formatUrlFromBps(brs);
        String newInfo = Utils.formatUrlFromBps(info);
        String newIndicator = Utils.formatUrlFromBps(indicator);
        String newUpdate = Utils.formatUrlFromBps(update);

        System.out.println(newUrl);
        System.out.println(newBrs);
        System.out.println(newIndicator);
        System.out.println(newInfo);
        System.out.println(newUpdate);
    }
}