package uk.ac.cam.groupseven.weatherapp.models

/* Yes, I used a string. And what? I could have used an enum, but that would have been looong.
    We'll probs display as a string anyway..
    Okay, I'll switch it to an enum if it becomes necessary. */
data class Wind(val speedMPS: Float?, val direction: String?)