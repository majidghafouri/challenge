package com.getyourguide.challenge.modules

import okhttp3.HttpUrl

interface ResponseProgressListener {
  fun update(url: HttpUrl, bytesRead: Long, contentLength: Long)
}
