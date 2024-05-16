package com.outerspace.ip_challenge

import com.outerspace.ip_challenge.data_layer.IPEntity

fun bunchOfIpEntities() = listOf(v1, v2, v3)

val v1 = IPEntity (
    "159.235.235.73",
    "success",
    "United States",
    "US",
    "GA",
    "Georgia",
    "Smyrna",
    "30080",
    33.8775,
    -84.5017,
    "America/New_York",
    "Charter Communications",
    "Spectrum",
    "AS20115 Charter Communications",
    null
)

val v2 = IPEntity (
    "24.48.1.0",
    "success",
    "Canada",
    "CA",
    "QC",
    "Quebec",
    "Montreal",
    "H1K",
    45.6085,
    -73.5493,
    "America/Toronto",
    "Le Groupe Videotron Ltee",
    "Videotron Ltee",
    "AS5769 Videotron Ltee",
    null
)

val v3 = IPEntity (
    "52.14.144.171",
    "success",
    "United States",
    "US",
    "OH",
    "Ohio",
    "Dublin",
    "43017",
    40.0992,
    -83.1141,
    "America/New_York",
    "Amazon.com, Inc.",
    "AWS EC2 (us-east-2)",
    "AS16509 Amazon.com, Inc.",
    null
)