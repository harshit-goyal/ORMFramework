create table student(
roll_number int primary key auto_increment,
first_name char(20) not null,
lst_name char(20) not null,
phone_number char(10) not null,
rgs_num int not null unique,
lbr_crd_num char(10) not null unique,
std_code int(5) not null,
fee_dcd decimal(10,2) not null default 0,
unique(std_code,phone_number)
);