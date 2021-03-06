package io.edanni.rndl.server.domain.service

import io.edanni.rndl.common.domain.entity.Entry
import io.edanni.rndl.server.application.dto.TorqueEntryData
import io.edanni.rndl.server.domain.repository.EntryRepository
import io.edanni.rndl.server.domain.repository.TripRepository
import io.edanni.rndl.server.domain.repository.VehicleRepository
import io.edanni.rndl.server.domain.service.trip.TripStatisticsUpdater
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EntryService(
        private val vehicleRepository: VehicleRepository,
        private val tripRepository: TripRepository,
        private val entryRepository: EntryRepository,
        private val tripStatisticsUpdater: TripStatisticsUpdater
) {
    //
    // CONTROLLER ENTRY POINTS
    //

    @Transactional
    fun insertEntry(torqueEntryData: TorqueEntryData): Entry {
        val vehicleId = vehicleRepository.findIdByTorqueId(torqueEntryData.torqueId)
        val tripId = tripRepository.findOrCreateTripIdByVehicleIdAndTimestamp(vehicleId, torqueEntryData.tripTimestamp!!)
        val entry = entryRepository.insert(torqueEntryData.toEntry(tripId))
        tripStatisticsUpdater.update(tripId)
        return entry
    }

    //
    // INTERNAL
    //
}