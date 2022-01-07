package cn.moyunying.assistant.dao;

import cn.moyunying.assistant.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginTicketMapper {

    int insertLoginTicket(LoginTicket loginTicket);

    int updateStatus(String cookie, int status);

    LoginTicket selectLoginTicketByCookie(String cookie);

    LoginTicket selectLoginTicketByUserId(int userId);
}
