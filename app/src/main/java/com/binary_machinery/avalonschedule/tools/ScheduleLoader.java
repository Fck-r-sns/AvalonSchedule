/*
 * Copyright 2016 Evgeny Prikhodko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
                    try {
                        return Jsoup.connect(url).get();
                    } catch (IOException e) {
                        throw new RuntimeException(e); // rethrow unchecked
                    }
                })
                .retry(2);
    }
}
