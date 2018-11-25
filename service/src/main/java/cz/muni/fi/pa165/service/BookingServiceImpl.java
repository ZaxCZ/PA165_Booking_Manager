package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.dao.BookingDao;
import cz.muni.fi.pa165.entity.Booking;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.TransactionRequiredException;
import cz.muni.fi.pa165.service.exceptions.BookingManagerDataAccessException;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Martin Palenik
 */
@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingDao bookingDao;

    @Override
    public void book(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("booking is null");
        }

        try {
            bookingDao.create(booking);
        } catch (TransactionRequiredException | EntityExistsException | IllegalArgumentException e) {
            throw new BookingManagerDataAccessException("BookingDAO could not create a new booking.", e);
        }
    }

    @Override
    public void cancel(Booking booking) {
        if (booking == null){
            throw new IllegalArgumentException("booking cannot be null");
        }

        try {
            bookingDao.remove(booking);
        } catch (TransactionRequiredException | IllegalArgumentException e) {
            throw new BookingManagerDataAccessException("Error during service.", e);
        }
    }

    @Override
    public List<Booking> getAll() {
        return bookingDao.findAll();
    }

    @Override
    public Booking findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null.");
        }
        return bookingDao.findById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Booking booking) {
        if(booking == null) {
            throw new IllegalArgumentException("Booking cannot be null.");
        }
        // will be superseded by discount mechanism upon its completion
        // return Math.max(0, booking.getTotal() - calculateDiscount(booking));
        return bookingDao.findById(booking.getId()).getTotal();
    }
}
