package cn.shh.project.reggie.service.impl;

import cn.shh.project.reggie.mapper.AddressBookMapper;
import cn.shh.project.reggie.pojo.AddressBook;
import cn.shh.project.reggie.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

    @Override
    public boolean save(AddressBook entity) {
        return super.save(entity);
    }
}
