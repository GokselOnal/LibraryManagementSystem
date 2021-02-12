package edu.ozu.cs202project.controllers;

import edu.ozu.cs202project.Salter;
import edu.ozu.cs202project.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes({"usernamee","level","systemUsername","userId","userType","topicData",
        "bookData","searchData","borrowField","borrowHistoryData","bookreturn","allBookData",
        "count","usernamePub","usernameReg","messageSignup","emptyData","usernameMan","title",
        "author_name","forAddReq","forRemoveReg","count_borrow","genre_fav","data_author",
        "borrow_history_user","borrowCount","mostBorGen","mostBookName3","mostBorPub",
        "overdueCount","overdueCountForGiven","title","users","empty","removeMessage",
        "errorHistory","errorCount","requestData","pubNameData","errorPub","messagePub",
        "messageRemBook","errorRem","messageBook","errorBook","messageAssign","errorAssign",
        "errorUnassign","messageUnassign","errorAccept","messageAccept","errorAcceptRem",
        "messageAcceptRem","errorRequest","messageRequest","overdueData","errorRemReq",
        "messageRemReq","overdueMessage","errorMessageborrow3","intervalData","publishersData",
        "count_overdue","overdue_count_title","returnMessage","errorMessageReturn"})

public class AppController {


    @Autowired
    LoginService service;
    @Autowired
    SignUpService signUpService;
    @Autowired
    RegularUserServices regUserServices;
    @Autowired
    PublisherServices pubServices;
    @Autowired
    ManagerService managerService;
    @Autowired
    JdbcTemplate conn;


    @GetMapping("/")
    public String index(ModelMap model)
    {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage(ModelMap model){
        return "login";
    }

    @PostMapping("/login")
    public String login(ModelMap model, @RequestParam String username, @RequestParam String password){
        model.put("usernamee",username);
        password = Salter.salt(password, "CS202Project");
        String systemUsername = service.getName(username);
        model.put("systemUsername",systemUsername);
        if(!service.validate(username,password)){
            model.put("errorMessage", "Invalid Credentials");
            return  "login";
        }
        else if(service.getType(username).equals("1")){
            model.put("usernameReg",username);
            return "redirect:/regular_user";
        }
        else if(service.getType(username).equals("2")){
            model.put("usernamePub",username);

            return "redirect:/publisher";
        }
        else {
            model.put("usernameMan",username);
            return "redirect:/manager";
        }
    }

    @GetMapping("/signUp")
    public String signUp(ModelMap model){
        return "signUp";
    }
    @GetMapping("/regular_user")
    public String reqular_userPage(ModelMap model){
        String userId = (String) model.getAttribute("usernameReg");

        int count_borrow = regUserServices.totNumBook(userId);
        model.put("count_borrow",count_borrow);

        if(regUserServices.favourite_genre(userId) != null) {
            String favGenre = regUserServices.favourite_genre(userId);
            model.put("genre_fav", favGenre);
        }
        else if(regUserServices.favourite_genre(userId) == null){
            String a = "Borrow Historyiniz boş";
            model.put("genre_fav", a);
        }
        int count_overdue = regUserServices.overdueCount(userId);
        model.put("count_overdue",count_overdue);
        return "regular_user";
    }

    @PostMapping("/regular_user")
    public String regular_user(ModelMap model){
        return "redirect:/regular_user";
    }

    @GetMapping("/publisher")
    public String publisherPage(ModelMap model){
        String publisherId = (String) model.getAttribute("usernamePub");
        int count = pubServices.borrowCount(publisherId);
        model.put("count",count);
        return "publisher";
    }

    @PostMapping("/publisher")
    public String publisher(ModelMap model){
        List<String[]> data = pubServices.viewBooks();
        model.addAttribute("allBookData", data.toArray(new String[0][9]));
        return "redirect:/allBooks";
    }

    @GetMapping("/manager")
    public String manager(ModelMap model){
        return "manager";
    }

    @PostMapping("/signUp")
    public String signUp(ModelMap model, @RequestParam String userId, @RequestParam String password, @RequestParam String username){
        password = Salter.salt(password,"CS202Project");
        if(userId.length() != 8){
            model.put("messageSignup","");
            model.put("errorMessage2", "8 harfli gir");
            return "signUp";
        }
        else if(signUpService.validate(userId)){
            model.put("errorMessage3", "bu kullanıcı var");
            model.put("messageSignup","");
            return "signUp";
        }
        else {
            model.put("messageSignup","Başarıyla kaydedildin");
            signUpService.signUp(userId, password, username);
            model.put("userId", userId);
            return "signUp";
        }
    }

    @GetMapping("/searchList")
    public String searchList(ModelMap map){
        return "searchList";
    }

    @GetMapping("/borrowHistory")
    public String borrowHistorypage(ModelMap model){
        String userId = (String) model.getAttribute("usernameReg");
        List<String[]> data =regUserServices.userHistory(userId);
        model.addAttribute("borrowHistoryData", data.toArray(new String[0][10]));
        model.put("emptyData","Borrow Historyin suan bos");
        return "borrowHistory";
    }

    @PostMapping("/borrowHistory")
    public String borrowHistory(ModelMap model, @RequestParam String returnbook){
        LocalDate localDate = LocalDate.now();
        String returnDate = String.valueOf(localDate);
        model.put("returnbook",returnbook);
        if(!regUserServices.returnBook(returnbook,returnDate)){
            return"redirect:/borrowHistory";
        }
        if(regUserServices.returnBook(returnbook,returnDate)){
            return "redirect:/borrowHistory";
        }
        return "redirect:/borrowHistory";
    }

    @GetMapping("/allBooks")
    public String  allBooks(ModelMap model){
        return "allBooks";
    }
    @GetMapping("/list")
    public String list(ModelMap model){
        List<String[]> data = conn.query("SELECT * FROM topic",
                (row,index) -> {
                    return new String[] {row.getString("topic_family_id"),row.getString("topic_name")};
                });
        model.addAttribute("topicData", data.toArray(new String[0][2]));
        return "list";
    }
    @GetMapping("/searchBooks")
    public String searchListPage(Model model){
        return "searchBooks";
    }
    @PostMapping("/searchBooks")
    public String searchBooks(Model model , @RequestParam(required = false) String searchfield, @RequestParam("type") String type) {
        if (type != null && type.equals("advance")) {
            List<String[]> data = regUserServices.searchBook(searchfield);
            if (data != null) {
                model.addAttribute("searchData", data.toArray(new String[0][9]));
                return "redirect:/searchBooks";
            } else {
                model.addAttribute("searchData", null);
            }
        }
        else if (type != null && type.equals("title")) {
            List<String[]> data_title= regUserServices.searchBookForTitle(searchfield);
            if (data_title != null) {
                model.addAttribute("searchData", data_title.toArray(new String[0][9]));
                return "redirect:/searchBooks";
            } else {
                model.addAttribute("searchData", null);
            }
        }
        else if (type != null && type.equals("author")) {
            List<String[]> data_author = regUserServices.searchBookForAuthor(searchfield);
            if (data_author != null) {
                model.addAttribute("searchData", data_author.toArray(new String[0][9]));
                return "redirect:/searchBooks";
            } else {
                model.addAttribute("searchData", null);
            }
        }
        else if (type != null && type.equals("genre")) {
            List<String[]> data_genre = regUserServices.searchBookForGenre(searchfield);
            if (data_genre != null) {
                model.addAttribute("searchData", data_genre.toArray(new String[0][9]));
                return "redirect:/searchBooks";
            } else {
                model.addAttribute("searchData", null);
            }
        }
        else if(type != null && type.equals("publisher")) {
            List<String[]> data_publish = regUserServices.searchBookForPublisher(searchfield);
            if (data_publish != null) {
                model.addAttribute("searchData", data_publish.toArray(new String[0][9]));
                return "redirect:/searchBooks";
            } else {
                model.addAttribute("searchData", null);
            }
        }
        else if (type != null && type.equals("topic")) {
            List<String[]> data_topic = regUserServices.searchBookForTopic(searchfield);
            if (data_topic != null) {
                model.addAttribute("searchData", data_topic.toArray(new String[0][9]));
                return "redirect:/searchBooks";
            } else {
                model.addAttribute("searchData", null);
            }
        }
        else if (type != null && type.equals("available")) {
            List<String[]> data_available = regUserServices.searchBookForAvailable();
            if (data_available != null) {
                model.addAttribute("searchData", data_available.toArray(new String[0][9]));
                return "redirect:/searchBooks";
            } else {
                model.addAttribute("searchData", null);
            }
        } else if (type != null && type.equals("year")) {
            List<String[]> data_year = regUserServices.searchBookForYear(searchfield);
            if (data_year != null) {
                model.addAttribute("searchData", data_year.toArray(new String[0][9]));
                return "redirect:/searchBooks";
            } else {
                model.addAttribute("searchData", null);
            }
        }
        return "redirect:/searchBooks";
    }

    @GetMapping("/addRequest")
    public String addRequestPage(ModelMap model){
        return "addRequest";
    }

    @PostMapping("/addRequest")
    public String addRequest(ModelMap model, @RequestParam String title, @RequestParam String author_name, @RequestParam String genre_name, @RequestParam String topic_name,@RequestParam String publish_date,@RequestParam String page_number){
        String publisher_id = (String)model.get("usernamee");
        if(pubServices.addRequest(title,author_name,genre_name,topic_name,publisher_id,publish_date,page_number)){
            model.put("errorRequest","");
            model.put("messageRequest","istek gönderildi");
            return "addRequest";
        }
        else{
            model.put("messageRequest","");
            model.put("errorRequest","sistemde zaten var o kitap");
            return "addRequest";
        }
    }

    @GetMapping("/removeRequest")
    public String removeRequestPage(ModelMap model){
        return "removeRequest";
    }
    @PostMapping("/removeRequest")
    public String removeRequest(ModelMap model, @RequestParam String title , @RequestParam String author_name) {
        String publisher_id = (String)model.get("usernamee");

        if(pubServices.removeRequest(title,author_name,publisher_id) == -5){
            model.put("messageRemReq","");
            model.put("errorRemReq","sizin yayıneviine ait bir kitap degil");
            return "removeRequest";
        }
        if(pubServices.removeRequest(title,author_name,publisher_id) == -1){
            model.put("messageRemReq","");
            model.put("errorRemReq","o title sistemde yok");
            return "removeRequest";
        }
        if(pubServices.removeRequest(title,author_name,publisher_id) == -2){
            model.put("messageRemReq","");
            model.put("errorRemReq","o author sistemde yok");
            return "removeRequest";
        }
        if(pubServices.removeRequest(title,author_name,publisher_id) == -3){
            model.put("messageRemReq","");
            model.put("errorRemReq","o authora ait o titleda bir kitap yok");
            return "removeRequest";
        }
        if(pubServices.removeRequest(title,author_name,publisher_id) == 1){
            model.put("errorRemReq","");
            model.put("messageRemReq","istek gönderildi");
            return "removeRequest";
        }
        return "removeRequest";
    }

    @GetMapping("/lookRequest")
    public String lookRequestPage(ModelMap model){
        return "lookRequest";
    }
    @PostMapping("/lookRequest")
    public String lookRequest(ModelMap model, @RequestParam("type") String type) {
        if (type != null && type.equals("add")) {

            List<String[]> data = managerService.lookForAddRequest();
            if (data != null) {
                model.addAttribute("requestData", data.toArray(new String[0][7]));
                return "redirect:/lookRequest";
            } else {
                model.addAttribute("requestData", null);
            }
        } else if (type != null && type.equals("remove")) {
            List<String[]> data_rem = managerService.lookForRemRequest();
            if (data_rem != null) {
                model.addAttribute("requestData", data_rem.toArray(new String[0][7]));
                return "redirect:/lookRequest";
            } else {
                model.addAttribute("requestData", null);
            }
        }
        return "redirect:/lookRequest";
    }

    @GetMapping("/borrowHistoryForManager")
    public String borrowHistoryForManagerPage(ModelMap model){
        return "borrowHistoryForManager";
    }

    @PostMapping("/borrowHistoryForManager")
    public String borrowHistoryForManager(ModelMap model, @RequestParam String userId){
        List<String[]> data = managerService.viewBorrowList(userId);
        if(data != null){
            model.addAttribute("borrow_history_user", data.toArray(new String[0][4]));
            return "redirect:/borrowHistoryForManager";
        }
        else if(data == null){
            model.put("errorHistory","yanlıs işlem");
            return "borrowHistoryForManager";
        }
        return "borrowHistoryForManager";
    }

    @GetMapping("/borrowCount")
    public String borrowCountPage(ModelMap model){

        return "borrowCount";
    }

    @PostMapping("/borrowCount")
    public String borrowCount(ModelMap model , @RequestParam String bookId2){
        String borrowCount = managerService.totalBorrowCount(bookId2);
        if(borrowCount != null){
            model.put("borrowCount",borrowCount);
            return "redirect:/borrowCount";
        }
        else if(borrowCount == null){
            model.put("errorCount","yanlıss işlem");
            return "borrowCount";
        }
        return "borrowCount";
    }

    @GetMapping("/statistics")
    public String staticticsPage(ModelMap model){
        String mostBorGen = managerService.mostBorrowedGenre();
        model.put("mostBorGen",mostBorGen);
        String mostBookName3 = managerService.mostBorrowedBookLast3Mount();
        model.put("mostBookName3",mostBookName3);
        String mostBorPub = managerService.mostBorrowerPublisher();
        model.put("mostBorPub",mostBorPub);
        int overdueCount = managerService.overdueBookCount();
        model.put("overdueCount",overdueCount);
        return "statistics";
    }

    @PostMapping("/statistics")
    public String statictics(ModelMap model, @RequestParam String title){
        String overdueCountForGiven = managerService.overdueForGivenBook(title);
        model.put("overdueCountForGiven",overdueCountForGiven);
        return "redirect:/statistics";
    }

    @GetMapping("/mostBorrewedBookUsers")
    public String mostBorrewedBookUsersPage(ModelMap model){
        List<String[]> data = managerService.listUsersWhoBorMostBorrowed();
        if(data != null) {
            model.addAttribute("users", data.toArray(new String[0][2]));
        }
        return "mostBorrewedBookUsers";
    }

    @PostMapping("/mostBorrewedBookUsers")
    public String mostBorrewedBookUsers(ModelMap model){
        return "mostBorrewedBookUsers";
    }

    @GetMapping("/addBook")
    public String addBookPage(ModelMap model){
        List<String[]> data = conn.query("SELECT name FROM users WHERE user_type = '2'",
                (row,index) -> {
                    return new String[] {row.getString("name")};
                });
        model.addAttribute("pubNameData", data.toArray(new String[0][1]));
        return "addBook";
    }

    @PostMapping("/addBook")
    public String addBook(ModelMap model, @RequestParam String title, @RequestParam String author_name, @RequestParam String genre_name, @RequestParam String publisher,@RequestParam String topic_name,@RequestParam String publish_date,@RequestParam String page_number){
        System.out.println(publisher);
        if(managerService.addBook(title,author_name,genre_name,topic_name,publisher,publish_date,page_number)){
            model.put("messageBook","Kitap eklendi");
            model.put("errorBook","");
            return "addBook";
        }
        else{
            model.put("errorBook","Bu kitap sistemde var");
            model.put("messageBook","");
            return "addBook";
        }
    }

    @GetMapping("/removeBook")
    public String removeBookPage(ModelMap model){
        return "removeBook";
    }

    @PostMapping("/removeBook")
    public String removeBook(ModelMap model, @RequestParam String removefield){

        if(managerService.removeBook(removefield) == 1){
            model.put("errorRem" , "");
            model.put("messageRemBook", "Book is removed");
            return "removeBook";
        }
        else if(managerService.removeBook(removefield) == -1){
            model.put("messageRemBook","");
            model.put("errorRem" , "hatalı id girdiniz");
            return "removeBook";
        }
        else if(managerService.removeBook(removefield) == 0){
            model.put("messageRemBook","");
            model.put("errorRem" , "kitap suan birinin elinde");
            return "removeBook";
        }
        return "removeBook";
    }

    @GetMapping("/borrowBook")
    public String borrowBookPage(ModelMap model){
        return"borrowBook";
    }

    @PostMapping("/borrowBook")
    public String borrowBook(ModelMap model, @RequestParam String borrowField){
        String userId = (String) model.getAttribute("usernameReg");
        LocalDate localDate = LocalDate.now();
        String borrowDate = String.valueOf(localDate);
        int result = regUserServices.borrowBook(userId,borrowField,borrowDate);
        if(result == 1){
            model.put("errorMessageborrow2", "");
            model.put("errorMessageborrow3", "");
            model.put("Messageborrow1", "Başarıyla alındı");
            return "borrowBook";
        }
        if(result== -1){
            model.put("Messageborrow1", "");
            model.put("errorMessageborrow2", "");
            model.put("errorMessageborrow3", "o kitap sistemde yok");
            return "borrowBook";
        }
        if(result == 0){
            model.put("Messageborrow1", "");
            model.put("errorMessageborrow3", "");
            model.put("errorMessageborrow2", "kitap Available değil,hold_requeste eklendin");
            return "borrowBook";
        }
        return"borrowBook";
    }

    @GetMapping("/addPublisher")
    public String addPublisherPage(ModelMap model){
        return "addPublisher";
    }

    @PostMapping("/addPublisher")
    public String addPublisher(ModelMap model, @RequestParam String publisherID , @RequestParam String password, @RequestParam String publisher_name){
        if(publisherID.length() != 8){
            model.put("messagePub","");
            model.put("errorPub", "8 harf gir");
            return "addPublisher";
        }
        else if(signUpService.validate(publisherID)){
            model.put("messagePub","");
            model.put("errorPub", "bu kullanıcı var");
            return "addPublisher";
        }
        else {
            model.put("messagePub", "başarıyla eklendi");
            model.put("errorPub", "");
            managerService.addPublisher(publisherID, password,publisher_name);
        }
        return "addPublisher";
    }

    @GetMapping("/assignBook")
    public String assignBookPage(ModelMap model){
        return "assignBook";
    }

    @PostMapping("/assignBook")
    public String assignBook(ModelMap model, @RequestParam String userId, @RequestParam String bookId){
        LocalDate localDate = LocalDate.now();
        String assignDate = String.valueOf(localDate);
        if(managerService.assignBooktoUser(userId,bookId,assignDate) == 1){
            model.put("messageAssign","Assign edildi");
            model.put("errorAssign","");
            return "assignBook";
        }
        else if(managerService.assignBooktoUser(userId,bookId,assignDate) == 0){
            model.put("errorAssign","Sistemde o kitap yok");
            model.put("messageAssign","");
            return "assignBook";
        }
        else if(managerService.assignBooktoUser(userId,bookId,assignDate) == -2){
            model.put("errorAssign"," o kitap suan avaialble degil");
            model.put("messageAssign","");
            return "assignBook";
        }
        else if(managerService.assignBooktoUser(userId,bookId,assignDate) == -1){
            model.put("messageAssign","");
            model.put("errorAssign","Sistemde o kullanıcı yok");
            return "assignBook";
        }
        return "assignBook";
    }

    @GetMapping("/unassignBook")
    public String unassignBookPage(ModelMap model){
        return "unassignBook";
    }

    @PostMapping("/unassignBook")
    public String unassignBook(ModelMap model, @RequestParam String userId, @RequestParam String bookId){
        LocalDate localDate = LocalDate.now();
        String returnDate = String.valueOf(localDate);
        if(managerService.unassignBooktoUser(userId, bookId,returnDate) == 1){
            model.put("messageUnassign","unAssign edildi");
            model.put("errorUnassign","");
            return "unassignBook";
        }
        else if(managerService.unassignBooktoUser(userId, bookId,returnDate) == -2){
            model.put("errorUnassign","Sistemde o kitap yok");
            model.put("messageUnassign","");
            return "unassignBook";
        }
        else if(managerService.unassignBooktoUser(userId, bookId,returnDate) == -3){
            model.put("errorUnassign"," o kitap suan assign edilmemiş değil zaten");
            model.put("messageUnassign","");
            return "unassignBook";
        }
        else if(managerService.unassignBooktoUser(userId, bookId,returnDate) == -1){
            model.put("messageUnassign","");
            model.put("errorUnassign","Sistemde o kullanıcı yok");
            return "unassignBook";
        }
        else if(managerService.unassignBooktoUser(userId, bookId,returnDate) == -4){
            model.put("messageUnassign","");
            model.put("errorUnassign","o kitabı almıs olan kişi bu kullanıcı degil");
            return "unassignBook";
        }
        return "unassignBook";
    }

    @GetMapping("/acceptRequest")
    public String acceptRequestPage(ModelMap model){
        return "acceptRequest";
    }

    @PostMapping("/acceptRequest")
    public String acceptRequest(ModelMap model, @RequestParam String bookId) {
        if(managerService.acceptRequest(bookId) == 1){
            //kabul edildi
            model.put("errorAccept","");
            model.put("messageAccept","Kabul edildi");
            return "acceptRequest";
        }
        if(managerService.acceptRequest(bookId) == 0){
            //hatalı book id
            model.put("messageAccept","");
            model.put("errorAccept","Hatalı book id");
            return "acceptRequest";
        }
        if(managerService.acceptRequest(bookId) == -1){
            //eklenmek istenilen kitaplar arasında bu yok
            model.put("messageAccept","");
            model.put("errorAccept","request istegi olan bir kitap degil");
            return "acceptRequest";
        }
        return "acceptRequest";
    }

    @GetMapping("/acceptRemove")
    public String acceptRemovePage(ModelMap model){
        return "acceptRemove";
    }

    @PostMapping("/acceptRemove")
    public String acceptRemove(ModelMap model,@RequestParam String bookId){
        if(managerService.acceptRemoveReq(bookId) == 1){
            model.put("errorAcceptRem","");
            model.put("messageAcceptRem","Kabul edildi");
            return "acceptRemove";
        }
        if(managerService.acceptRemoveReq(bookId) == 0){
            model.put("messageAcceptRem","");
            model.put("errorAcceptRem","Hatalı book id");
            return "acceptRemove";
        }
        if(managerService.acceptRemoveReq(bookId) == -1){

            model.put("messageAcceptRem","");
            model.put("errorAcceptRem","suan birinin elinde");
            return "acceptRemove";
        }
        if(managerService.acceptRemoveReq(bookId) == -2){
            model.put("messageAcceptRem","");
            model.put("errorAcceptRem","cıkarılması istenmiş bir kitap degil");
            return "acceptRemove";
        }
        return "acceptRemove";
    }

    @GetMapping("/overdueList")
    public String overdueListPage(ModelMap model){
        managerService.checkOverdue();
        List<String[]> list = managerService.overdueList();
        if(list != null){
            model.put("overdueMessage","");
            model.addAttribute("overdueData", list.toArray(new String[0][2]));
        }
        else if(list == null){
            model.put("overdueMessage","Empty");
            return "overdueList";
        }
        return "overdueList";
    }

    @PostMapping("/overdueList")
    public String overdueList(ModelMap model){
        return "overdueList";
    }

    @GetMapping("/historywithinverval")
    public String historywithinvervalPage(ModelMap model){
        return "historywithinverval";
    }

    @PostMapping("/historywithinverval")
    public String historywithinverval(ModelMap model, @RequestParam String trip_start, @RequestParam String trip_finish){
        List<String[]> data = managerService.historyForInterval(trip_start,trip_finish);
        if(data != null){
            model.addAttribute("intervalData", data.toArray(new String[0][2]));
        }
        else{
            model.addAttribute("intervalData",null);
        }
        return "historywithinverval";
    }

    @GetMapping("/publishersForGenre")
    public String publishersForGenrePage(ModelMap modelMap){
        return"publishersForGenre";
    }

    @PostMapping("/publishersForGenre")
    public String publishersForGenre(ModelMap model , @RequestParam String genre_field){
        ArrayList<String> data = regUserServices.searchPublisherForGenre(genre_field);
        if(data != null){
            model.addAttribute("publishersData", data);
        }
        else if(data == null){
            model.addAttribute("publishersData",null);
        }
        return"publishersForGenre";
    }

    @GetMapping("/overdueForTitle")
    public String overdueForTitlePage(ModelMap model){
        return "overdueForTitle";
    }

    @PostMapping("/overdueForTitle")
    public String overdueForTitle(ModelMap model , @RequestParam String title ){
        int overdue_count_title0 = managerService.overdueCountForTitle(title);
        String overdue_count_title = String.valueOf(overdue_count_title0);
        model.put("overdue_count_title",overdue_count_title);
        return "redirect:/overdueForTitle";
    }
}