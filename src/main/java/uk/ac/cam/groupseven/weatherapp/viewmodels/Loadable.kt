package uk.ac.cam.groupseven.weatherapp.viewmodels

data class Loadable<T> (
    val loading: Boolean,
    val error: Throwable?,
    val viewModel: T?){
    constructor() : this(true, null, null)
    constructor(error: Throwable) : this(false, error, null)
    constructor(viewModel: T) : this(false, null, viewModel)
}
