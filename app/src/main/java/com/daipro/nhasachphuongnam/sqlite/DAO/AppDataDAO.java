package com.daipro.nhasachphuongnam.sqlite.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.daipro.nhasachphuongnam.model.Bill;
import com.daipro.nhasachphuongnam.model.BillDetail;
import com.daipro.nhasachphuongnam.model.Book;
import com.daipro.nhasachphuongnam.model.BookType;
import com.daipro.nhasachphuongnam.model.User;

import java.util.List;

@Dao
public interface AppDataDAO {
    @Insert
    long addUser(User user);

    @Delete
    void deleteUSer(User user);

    @Update
    void updateUser(User user);

    @Query("select * from User")
    List<User> getAllUser();

    @Query("select * from User where id=:id limit 1")
    User getUserById(int id);
    @Query("select * from User where userName like :userName limit 1")
    User getUserByUserName(String userName);
    @Query("select * from User where kw like :kw")
    List<User> getUserSearch(String kw);

    @Insert
    long addBookType(BookType bookType);

    @Delete
    void deleteBookType(BookType bookType);

    @Update
    void upDateBookType(BookType bookType);

    @Query("select * from BookType order by name")
    List<BookType> getAllBookType();

    @Query("Select * from booktype where id=:id limit 1")
    BookType getBookTypeById(int id);

    @Query("select * from BookType where kw like :kw")
    List<BookType> getBookTypeByKeyWord(String kw);

    @Insert
    long addBook(Book book);

    @Delete
    int deleteBook(Book book);

    @Update
    int upDateBook(Book book);

    @Query("Select * from Book")
    List<Book> getAllBook();

    @Query("Select * from Book where id=:id limit 1")
    Book getBookById(int id);

    @Query("Select * from Book where kw like :kw and idBookType=:idBookType")
    List<Book> getBookByKw(String kw, int idBookType);

    @Query("Select * from Book where kw like :kw")
    List<Book> getBookByKw(String kw);

    @Query("select * from book where idBookType=:bookTypeId")
    List<Book> getBookByBookType(int bookTypeId);

    @Query("Delete from Book where idBookType=:idBookType")
    void deleteBookByBookType(int idBookType);


    @Insert
    long addBill(Bill bill);

    @Delete
    int deleteBill(Bill bill);

    @Update
    int updateBill(Bill bill);

    @Query("Select * from Bill")
    List<Bill> getAllBills();

    @Query("select * from Bill where id=:id limit 1")
    Bill getBillById(int id);

    @Query("select * from bill where idStaff=:idStaff")
    List<Bill>getBillByIdStaff(int idStaff);

    @Query("select * from Bill where kw like :kw")
    List<Bill> getBillByKw(String kw);


    @Insert
    long addBillDetail(BillDetail billDetail);

    @Delete
    int deleteBillDetail(BillDetail billDetail);

    @Update
    int updateBillDetail(BillDetail billDetail);

    @Query("select * from billdetail")
    List<BillDetail> getAllBillDetail();

    @Query("select * from BillDetail where idBill = :idBill")
    List<BillDetail> getBillDetailByIdBill(Integer idBill);

    @Query("select * from BillDetail where id=:id limit 1")
    BillDetail getBillDetailById(int id);

    @Query("delete from BILLDETAIL where idBill==:idBill")
    void deleteBillDetailByIdBill(int idBill);

    @Query("delete from BillDetail where idBook=:idBook")
    void deleteBillDetailByIdBook(int idBook);


    @Query("select b.name, b.id,sum(d.count) as 'count',sum(d.price) as 'price',b.author,b.idBookType from Book b inner join billdetail d on b.id=d.idBook group by idBook order by sum(d.count) desc limit 10")
    List<Book> getTop10Book();

    @Query("select sum(price) as 'count' from BILL where time=:time")
    double getTodayMoney(long time);

    @Query("Select sum(price) as 'count' from Bill where time>=:timeStart and time<=:timeEnd")
    double  getMonthMoney(double timeStart,double timeEnd);

    @Query("Select sum(price) as 'count' from Bill where time>=:timeStart and time<=:timeEnd")
    double  getYearMoney(double timeStart,double timeEnd);
}
