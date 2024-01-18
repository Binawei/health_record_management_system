package com.example.devohealthrecord.services;
import com.example.devohealthrecord.dto.request.HealthRecordDTO;
import com.example.devohealthrecord.dto.response.GenericResponse;
import com.example.devohealthrecord.dto.response.HealthRecordResponse;
import com.example.devohealthrecord.entities.HealthRecord;
import com.example.devohealthrecord.exception.CommonApplicationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface HealthRecordServics {
    GenericResponse createHealthRecord(HealthRecordDTO recordDTO, String email);

    GenericResponse<?> findHealthRecordById(Long recordId) throws CommonApplicationException;

    GenericResponse<?> UserFindHealthRecordById(Long recordId, String email) throws CommonApplicationException;

    GenericResponse<Page<HealthRecord>> viewAllHealthRecords(int page, int size) throws CommonApplicationException;

    GenericResponse<HealthRecordResponse> updateHealthRecord(Long recordId, HealthRecordDTO request, String email);

    GenericResponse<String> deleteRecord(Long recordId, String userEmail);
}
