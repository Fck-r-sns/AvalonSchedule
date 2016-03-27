package com.binary_machinery.avalonschedule.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by fckrsns on 11.03.2016.
 */
public class ScheduleLoader {

    public static Observable<Document> load(String sourceUrl) {
        return Observable.just(sourceUrl)
                .subscribeOn(Schedulers.io())
                .map(url -> {
                    Document result = null;
                    try {
                        result = Jsoup.connect(url).get();
                    } catch (IOException e) {
                        throw new RuntimeException(e.getMessage()); // rethrow unchecked
                    }
                    return result;
                });
    }
}
