package io.edanni.rndl.server.application.controller

import io.edanni.rndl.common.application.dto.GroupedTripList
import io.edanni.rndl.common.domain.entity.Trip
import io.edanni.rndl.server.domain.service.TripService
import io.edanni.rndl.server.infrastructure.request.currentUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.threeten.bp.LocalDateTime
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/trips")
class TripsController(private val tripService: TripService) {

    @GetMapping
    fun index(year: Int = LocalDateTime.now().year, month: Int = LocalDateTime.now().monthValue, vehicleId: Long?): Mono<List<GroupedTripList>> {
        return currentUser().map { tripService.listTripsFiltered(year, month, it, vehicleId) }
    }

    @GetMapping("/{id}")
    fun show(@PathVariable("id") id: Long): Mono<Trip> = currentUser().map { tripService.findByIdAndUser(id, it) }
}