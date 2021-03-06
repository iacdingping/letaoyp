package com.iac.letaoyp.service.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.iac.letaoyp.entity.user.CartItem;
import com.iac.letaoyp.repository.BasicRepository;
import com.iac.letaoyp.repository.user.CartItemDao;
import com.iac.letaoyp.service.BasicService;

@Component
@Transactional
public class CartItemService extends BasicService<CartItem,java.lang.Long> {

	@Autowired
	private CartItemDao cartItemDao;
	
	@Override
	public BasicRepository<CartItem,java.lang.Long> getRepository() {
		return cartItemDao;
	}

	public List<CartItem> findByCartId(Long cart) {
		return cartItemDao.findByCartId(cart);
	}
	
	public void updateActiveByIds(boolean active, Long[] ids) {
		cartItemDao.updateActiveByIdIn(active, ids);
	}

	public void delete(Long[] ids) {
		cartItemDao.deleteByIdIn(ids);
	}

	public void delete(Long cartId, Long id) {
		cartItemDao.deleteByCartIdAndId(cartId, id);
	}

	public void deleteByCartId(Long id) {
		cartItemDao.deleteByCartId(id);
	}
}
