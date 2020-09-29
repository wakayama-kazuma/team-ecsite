package jp.co.internous.grapes.controller;

import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import jp.co.internous.grapes.model.domain.MstDestination;
import jp.co.internous.grapes.model.domain.MstUser;
import jp.co.internous.grapes.model.form.DestinationForm;
import jp.co.internous.grapes.model.mapper.MstDestinationMapper;
import jp.co.internous.grapes.model.mapper.MstUserMapper;
import jp.co.internous.grapes.model.session.LoginSession;

@Controller
@RequestMapping("/grapes/destination")
public class DestinationController {
	
	@Autowired
	private MstDestinationMapper mstDestinationMapper;
	
	@Autowired
	private LoginSession loginSession;
	
	@Autowired
	private MstUserMapper mstUserMapper;
	
	private Gson gson = new Gson();

	//宛先登録画面に表示するものを取得
	@RequestMapping("/")
	public String index(Model m) {
		MstUser user = mstUserMapper.findByUserNameAndPassword(loginSession.getUserName(), loginSession.getPassword());
		
		m.addAttribute("loginSession", loginSession);
		m.addAttribute("user", user);

		return "destination";
	}
	
	//宛先選択したものを論理削除
	@SuppressWarnings("unchecked")
	@RequestMapping("/delete")
	@ResponseBody
	public boolean delete(@RequestBody String destinationId) {
		
		Map<String, String> map = gson.fromJson(destinationId, Map.class);
		String id = map.get("destinationId");

		int result = mstDestinationMapper.updateStatus(Integer.parseInt(id));

		return result > 0;
	}
	
	//宛先を登録
	@ResponseBody
	@PostMapping("/register")
	public String postRegister(@RequestBody DestinationForm destinationForm) {
		MstDestination mstDestination = new MstDestination();
		BeanUtils.copyProperties(destinationForm, mstDestination);
		int userId = loginSession.getUserId();
		mstDestination.setUserId(userId);
		int count = mstDestinationMapper.insert(mstDestination);
		
		//登録した宛先IDの取得
		Integer id = 0;
		if (count > 0) {
			id = mstDestinationMapper.findIdByUserId(userId);
		}
		return id.toString();
	}

}
