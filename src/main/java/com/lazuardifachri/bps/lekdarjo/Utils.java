package com.lazuardifachri.bps.lekdarjo;

import com.lazuardifachri.bps.lekdarjo.exception.BadRequestException;
import com.lazuardifachri.bps.lekdarjo.exception.ExceptionMessage;
import com.lazuardifachri.bps.lekdarjo.model.EDistrict;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String formatUrlFromBps(String url) {
        if (url.contains("sidoarjokab.bps.go.id") && !url.endsWith("=")) {
            String[] urlArray = url.split("=");
            String[] urlArrayCleaned = Arrays.copyOf(urlArray, urlArray.length -1);
            return String.join("=",urlArrayCleaned).concat("=");
        }
        return url;
    }

    public static Date parseComplicatedDate(Date date) throws ParseException {
        DateTimeFormatter f = DateTimeFormatter.ofPattern( "E MMM dd HH:mm:ss z uuuu" )
                .withLocale( Locale.US );
        ZonedDateTime zdt = ZonedDateTime.parse(date.toString(), f);

        LocalDate ld = zdt.toLocalDate();
        DateTimeFormatter fLocalDate = DateTimeFormatter.ofPattern( "dd/MM/uuuu" );
        String output = ld.format( fLocalDate);
        return new SimpleDateFormat("dd/MM/yyyy").parse(output);
    }

    public static EDistrict getEDistrictByCode(String code) {
        switch (code) {
            case "3515010":
                return EDistrict.TARIK;
            case "3515020":
                return EDistrict.PRAMBON;
            case "3515030":
                return EDistrict.KREMBUNG;
            case "3515040":
                return EDistrict.PORONG;
            case "3515050":
                return EDistrict.JABON;
            case "3515060":
                return EDistrict.TANGGULANGIN;
            case "3515070":
                return EDistrict.CANDI;
            case "3515080":
                return EDistrict.TULANGAN;
            case "3515090":
                return EDistrict.WONOAYU;
            case "3515100":
                return EDistrict.SUKODONO;
            case "3515110":
                return EDistrict.SIDOARJO;
            case "3515120":
                return EDistrict.BUDURAN;
            case "3515130":
                return EDistrict.SEDATI;
            case "3515140":
                return EDistrict.WARU;
            case "3515150":
                return EDistrict.GEDANGAN;
            case "3515160":
                return EDistrict.TAMAN;
            case "3515170":
                return EDistrict.KRIAN;
            case "3515180":
                return EDistrict.BALONG_BANDO;
            case "3515":
                return EDistrict.UMUM;
            default:
                throw new BadRequestException(ExceptionMessage.DISTRICT_NOT_FOUND);
        }
    }
}
