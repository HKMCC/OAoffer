package com.hcc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hcc.AdminUtlis;
import com.hcc.config.sercurity.component.JwtTokenuntil;
import com.hcc.mapper.AdminMapper;
import com.hcc.mapper.AdminRoleMapper;
import com.hcc.mapper.RoleMapper;
import com.hcc.pojo.Admin;

import com.hcc.pojo.AdminRole;
import com.hcc.pojo.RespBean;
import com.hcc.pojo.Role;
import com.hcc.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author CC
 * @since 2022-04-28
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenuntil jwtTokenuntil;

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AdminRoleMapper adminRoleMappep;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    /**
     * 登录返回Token
     *
     * @param username
     * @param password
     * @param request
     * @return
     */

    @Override
    public RespBean login(String username, String password, String code, HttpServletRequest request) {
        Object captcha = request.getSession().getAttribute("captcha");
        if (StringUtils.isEmpty(captcha) || !captcha.equals(code)) {
            return RespBean.error("验证码错误( ╯□╰ )");
        }
        //框架登录返回userDetails
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        //passwordEncoder.matches(password,userDetails.getPassword())==fals密码如果匹配失败
        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            return RespBean.error("用户名或者密码不正确");
        }
        if (!userDetails.isEnabled()) {
            return RespBean.error("账号被禁用( ╯□╰ )");
        }
        //更新security登录用户对象（全局）
        UsernamePasswordAuthenticationToken AuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(AuthenticationToken);


        //如果登录匹配成功拿Jwt令牌token
        String token = jwtTokenuntil.generateToken(userDetails);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);

        return RespBean.success("登录成功", tokenMap);
    }

    /**
     * 根据用户名获取用户
     *
     * @param username
     * @return
     */
    @Override
    public Admin getAdminbyUserName(String username) {
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", username)
                .eq("enabled", true));
    }

    /**
     * 根据用户ID查询角色列表
     * @param adminId
     * @return
     */
    @Override
    public List<Role> getRoles(Integer adminId) {

        return roleMapper.getRoles(adminId);
    }

    /**
     * 获取所有操作员
     * @param keywords
     * @return
     */
    @Override
    public List<Admin> getAllAdmins(String keywords) {
        System.out.println(AdminUtlis.getCurrentAdmin().getId());
        return adminMapper.getAllAdmins(AdminUtlis.getCurrentAdmin().getId(),keywords);
    }

    /**
     * 更新操作员角色
     * @param adminId
     * @param rids
     * @return
     */
    @Override
    public RespBean updateAdminRole(Integer adminId, Integer[] rids) {
        //先清空
        adminRoleMappep.delete(new QueryWrapper<AdminRole>().eq("adminId",adminId));
        //再添加
        Integer result=adminRoleMappep.updateAdminRole(adminId,rids);
        if (rids.length==result){
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }

    /**
     * 更新用户名密码
     * @param oldPass
     * @param pass
     * @param adminId
     * @return
     */
    @Override
    public RespBean updateAdminPasswork(String oldPass, String pass, Integer adminId) {
        Admin admin = adminMapper.selectById(adminId);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //判断旧密码是否正确
        if (encoder.matches(oldPass,admin.getPassword())){
            admin.setPassword(encoder.encode(pass));
            int result = adminMapper.updateById(admin);
            if (result==1){
                return RespBean.success("更新成功");
            }
        }

        return RespBean.error("更新失败");
    }

    /**
     * 更新用户头像
     * @param url
     * @param id
     * @param authentication
     * @return
     */
    @Override
    public RespBean updateAdminUserFace(String url, Integer id, Authentication authentication) {
        Admin admin = adminMapper.selectById(id);
        admin.setUserFace(url);
        int result = adminMapper.updateById(admin);
        if (result == 1){
           Admin principal = (Admin) authentication.getPrincipal();
           principal.setUserFace(url);
           SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(admin,null,authentication.getAuthorities()));
           return RespBean.success("更新成功",url);
        }
        return RespBean.error("更新失败");
    }
}


