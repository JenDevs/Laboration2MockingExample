package com.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class BookingSystemTest {

    @Mock private TimeProvider timeProvider;
    @Mock private RoomRepository roomRepository;
    @Mock private NotificationService notificationService;
    @Mock private Room room;
    @Mock private Booking booking;
    @Mock private Room room1;
    @Mock private Room room2;

    private BookingSystem bookingSystem;
    private LocalDateTime testTime;
    private String generateRoomId;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        bookingSystem = new BookingSystem(timeProvider, roomRepository, notificationService);

        testTime = LocalDateTime.of(2025,1,1,10,0);
        when(timeProvider.getCurrentTime()).thenReturn(testTime);

        generateRoomId = UUID.randomUUID().toString();
        when(room.getId()).thenReturn(generateRoomId);
        when(roomRepository.findById(generateRoomId)).thenReturn(Optional.of(room));
        when(room1.getId()).thenReturn(UUID.randomUUID().toString());
        when(room2.getId()).thenReturn(UUID.randomUUID().toString());

    }

    @AfterEach
    void tearDown () throws Exception {
        closeable.close();
    }

    @Test
    void shouldThrowExceptionWhenRoomIdIsNull () {
        LocalDateTime startTime = testTime;
        LocalDateTime endTime = testTime.plusHours(1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> bookingSystem.bookRoom(null, startTime, endTime));

        assertEquals("Bokning kräver giltiga start- och sluttider samt rum-id", exception.getMessage());

    }

    @Test
    void shouldThrowExceptionWhenStartTimeIsNull () {
        String roomId = generateRoomId;
        LocalDateTime endTime = testTime.plusHours(1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> bookingSystem.bookRoom(roomId, null, endTime));

        assertEquals("Bokning kräver giltiga start- och sluttider samt rum-id", exception.getMessage());

    }

    @Test
    void shouldThrowExceptionWhenEndTimeIsNull () {
        String roomId = generateRoomId;
        LocalDateTime startTime = testTime;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> bookingSystem.bookRoom(roomId, startTime, null));

        assertEquals("Bokning kräver giltiga start- och sluttider samt rum-id", exception.getMessage());

    }

    @Test
    void shouldThrowExceptionWhenStartTimeIsInPast() {
        String roomId = generateRoomId;
        LocalDateTime startTime = testTime.minusHours(1);
        LocalDateTime endTime = testTime.plusHours(1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> bookingSystem.bookRoom(roomId, startTime, endTime));

        assertEquals("Kan inte boka tid i dåtid", exception.getMessage());

    }

    @Test
    void shouldThrowExceptionWhenEndTimeIsBeforeStartTime() {
        String roomId = generateRoomId;
        LocalDateTime startTime = testTime;
        LocalDateTime endTime = testTime.minusHours(1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> bookingSystem.bookRoom(roomId, startTime, endTime));

        assertEquals("Sluttid måste vara efter starttid", exception.getMessage());

    }

    @Test
    void shouldThrowExceptionWhenRoomDoNotExist() {
        String noRoomId = java.util.UUID.randomUUID().toString();
        LocalDateTime startTime = testTime;
        LocalDateTime endTime = testTime.plusHours(1);

        when(roomRepository.findById(noRoomId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> bookingSystem.bookRoom(noRoomId, startTime, endTime));

        assertEquals("Rummet existerar inte", exception.getMessage());
        verify(roomRepository, times(1)).findById(noRoomId);

    }

    @Test
    void shouldReturnFalseWhenRoomIsUnavailable() {
        LocalDateTime startTime = testTime;
        LocalDateTime endTime = testTime.plusHours(1);

        when(room.isAvailable(startTime,endTime)).thenReturn(false);

        boolean result = bookingSystem.bookRoom(room.getId(), startTime,endTime);

        assertFalse(result);

        verify(room, times(1)).isAvailable(startTime,endTime);

    }

    @Test
    void shouldSucceedWhenRoomIsAvailable() {
        String roomId = generateRoomId;
        LocalDateTime startTime = testTime;
        LocalDateTime endTime = testTime.plusHours(1);

        when(room.isAvailable(startTime,endTime)).thenReturn(true);

        boolean result = bookingSystem.bookRoom(roomId,startTime,endTime);

        assertTrue(result);

        verify(room, times(1)).isAvailable(startTime,endTime);
        verify(roomRepository, times(1)).findById(roomId);

    }


    @Test
    void shouldSendConfirmationWhenRoomIsBooked() throws NotificationException {
        LocalDateTime startTime = testTime;
        LocalDateTime endTime = testTime.plusHours(1);

        when(room.isAvailable(startTime,endTime)).thenReturn(true);

        boolean result = bookingSystem.bookRoom(room.getId(), startTime,endTime);

        assertTrue(result);
        verify(notificationService,times(1)).sendBookingConfirmation(any(Booking.class));

    }

    @Test
    void shouldNotFailWhenSendingBookingConfirmation () throws NotificationException {
        LocalDateTime startTime = testTime;
        LocalDateTime endTime = testTime.plusHours(1);

        when(room.isAvailable(startTime,endTime)).thenReturn(true);

        doThrow(new NotificationException("Misslyckades att skicka konfirmation"))
                .when(notificationService)
                .sendBookingConfirmation(any(Booking.class));

        boolean result = bookingSystem.bookRoom(room.getId(), startTime,endTime);
        assertTrue(result);

    }

    @Test
    void throwsExceptionIfStartTimeIsNull () {
        LocalDateTime endTime = testTime.plusHours(1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> bookingSystem.getAvailableRooms(null,endTime));

        assertEquals("Måste ange både start- och sluttid", exception.getMessage());
    }


    @Test
    void throwsExceptionWhenEndTimeIsNull () {
        LocalDateTime startTime = testTime;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> bookingSystem.getAvailableRooms(startTime, null));

        assertEquals("Måste ange både start- och sluttid", exception.getMessage());

    }

    @Test
    void throwsExceptionIfEndTimeIsBeforeStartTime() {
        LocalDateTime startTime = testTime;
        LocalDateTime endTime = testTime.minusHours(1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> bookingSystem.getAvailableRooms(startTime, endTime));

        assertEquals("Sluttid måste vara efter starttid", exception.getMessage());

    }

    @Test
    void shouldReturnOnlyAvailableRooms () {
        LocalDateTime startTime = testTime;
        LocalDateTime endTime = testTime.plusHours(1);

        when(room1.isAvailable(startTime,endTime)).thenReturn(true);
        when(room2.isAvailable(startTime,endTime)).thenReturn(false);
        when(roomRepository.findAll()).thenReturn(List.of(room1,room2));

        List<Room>availableRooms = bookingSystem.getAvailableRooms(startTime,endTime);

        assertEquals(1, availableRooms.size());
        assertTrue(availableRooms.contains(room1));
        assertFalse(availableRooms.contains(room2));

    }

    @Test
    void throwsExceptionIfBookingIdIsNull () {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> bookingSystem.cancelBooking(null));

        assertEquals("Boknings-id kan inte vara null", exception.getMessage());

    }

    @Test
    void cancelBookingWhenRoomHasBooking () {
        String bookingId = UUID.randomUUID().toString();

        when(room1.hasBooking(bookingId)).thenReturn(false);
        when(room2.hasBooking(bookingId)).thenReturn(true);

        when(room2.getBooking(bookingId)).thenReturn(booking);
        when(booking.getStartTime()).thenReturn(testTime.plusHours(1));
        when(timeProvider.getCurrentTime()).thenReturn(testTime);

        when(roomRepository.findAll()).thenReturn(List.of(room1, room2));

        boolean result = bookingSystem.cancelBooking(bookingId);

        assertTrue(result);

        verify(room2, times(1)).removeBooking(bookingId);
        verify(roomRepository, times(1)).save(room2);

    }

    @Test
    void returnsFalseIfNoRoomsHasBooking () {
        String bookingId = generateRoomId;

        when(room1.hasBooking(bookingId)).thenReturn(false);

        assertFalse(bookingSystem.cancelBooking(bookingId));
        verify(roomRepository, times(1)).findAll();

    }

    @Test
    void throwsExceptionIfBookingAlreadyStartedOrCompleted(){
        String bookingId = generateRoomId;

        LocalDateTime pastStartTime = testTime.minusHours(1);
        LocalDateTime currentTime = testTime;

        when(room.hasBooking(bookingId)).thenReturn(true);
        when(room.getBooking(bookingId)).thenReturn(booking);
        when(booking.getStartTime()).thenReturn(pastStartTime);
        when(timeProvider.getCurrentTime()).thenReturn(currentTime);
        when(roomRepository.findAll()).thenReturn(List.of(room));

        Exception exception = assertThrows(IllegalStateException.class, () -> bookingSystem.cancelBooking(bookingId));

        assertEquals("Kan inte avboka påbörjad eller avslutad bokning", exception.getMessage());

    }

    @Test
    void sendsCancellationConfirmationWhenBookingCancelled() throws NotificationException {
        String bookingId = generateRoomId;

        when(roomRepository.findAll()).thenReturn(List.of(room));
        when(room.hasBooking(bookingId)).thenReturn(true);
        when(room.getBooking(bookingId)).thenReturn(booking);
        when(booking.getStartTime()).thenReturn(testTime.plusHours(1));
        when(timeProvider.getCurrentTime()).thenReturn(testTime);

        boolean result = bookingSystem.cancelBooking(bookingId);

        assertTrue(result);
        verify(notificationService, times(1)).sendCancellationConfirmation(booking);

    }

    @Test
    void doesNotFailIfNotificationServiceFails() throws NotificationException {
        String bookingId = generateRoomId;

        when(roomRepository.findAll()).thenReturn(List.of(room));
        when(room.hasBooking(bookingId)).thenReturn(true);
        when(room.getBooking(bookingId)).thenReturn(booking);
        when(booking.getStartTime()).thenReturn(testTime.plusHours(1));
        when(timeProvider.getCurrentTime()).thenReturn(testTime);

        doThrow(new NotificationException("Failed to send notification")).when(notificationService).sendCancellationConfirmation(booking);

        boolean result = bookingSystem.cancelBooking(bookingId);

        assertTrue(result);
        verify(notificationService, times(1)).sendCancellationConfirmation(booking);

    }

}
