package org.example.myspringboot;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.example.myspringboot.common.*;
import org.example.myspringboot.controller.*;
import org.example.myspringboot.entity.User;
import org.example.myspringboot.mapper.UserMapper;
import org.example.myspringboot.utils.TokenUtils;
import org.example.myspringboot.service.UserService;
import org.springframework.mock.web.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.method.HandlerMethod;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
class SecurityTests {
 UserMapper mapper; User user; JwtInterceptor interceptor;
 @BeforeEach void setup() {
  mapper=mock(UserMapper.class); user=new User(); user.setId(1); user.setRole("user");
  user.setPassword("private-password"); when(mapper.selectById(1)).thenReturn(user);
  TokenUtils tokens=new TokenUtils(); ReflectionTestUtils.setField(tokens,"userMapper",mapper);
  ReflectionTestUtils.setField(tokens,"secret","01234567890123456789012345678901"); tokens.init();
  interceptor=new JwtInterceptor();
 }
 @Test void passwordIsWriteOnly() throws Exception {
  ObjectMapper json=new ObjectMapper(); assertFalse(json.writeValueAsString(user).contains("password"));
  assertEquals("input",json.readValue("{\"password\":\"input\"}",User.class).getPassword());
 }
 @Test void missingAndForgedTokensRejected() {
  assertThrows(Exception.class,()->interceptor.preHandle(new MockHttpServletRequest("GET","/his/medicine/query"),new MockHttpServletResponse(),new Object()));
  assertThrows(Exception.class,()->TokenUtils.verify(TokenUtils.createToken("1","")+"tampered"));
 }
 @Test void roleCheckedOnServer() throws Exception {
  MockHttpServletRequest req=new MockHttpServletRequest("GET","/user/selectAll");
  req.addHeader("Token",TokenUtils.createToken("1",""));
  HandlerMethod handler=new HandlerMethod(new UserController(),UserController.class.getMethod("selectAll"));
  assertThrows(Exception.class,()->interceptor.preHandle(req,new MockHttpServletResponse(),handler));
  user.setRole("admin"); assertTrue(interceptor.preHandle(req,new MockHttpServletResponse(),handler));
 }
 @Test void registrationAndRecoveryDisabled() {
  for(String path:new String[]{"/user/register","/user/password"})
   assertThrows(Exception.class,()->interceptor.preHandle(new MockHttpServletRequest("POST",path),new MockHttpServletResponse(),new Object()));
 }
 @Test void cookieSupportsAuthenticatedImages() {
  MockHttpServletRequest req=new MockHttpServletRequest("GET","/file/download/image.png");
  req.setCookies(new javax.servlet.http.Cookie("demo_session",TokenUtils.createToken("1","")));
  assertTrue(interceptor.preHandle(req,new MockHttpServletResponse(),new Object()));
 }
 @Test void expiredTokenRejected() {
  String expired = com.auth0.jwt.JWT.create().withIssuer("medicine-demo").withAudience("1")
   .withExpiresAt(new java.util.Date(0)).sign(com.auth0.jwt.algorithms.Algorithm.HMAC256("01234567890123456789012345678901"));
  assertThrows(Exception.class,()->TokenUtils.verify(expired));
 }
 @Test void blankBatchIdCannotUpdateAllRows() {
  drugExpirationDateController controller=new drugExpirationDateController();
  com.alibaba.fastjson2.JSONObject input=new com.alibaba.fastjson2.JSONObject();
  input.put("MediStockBatchInfo",new com.alibaba.fastjson2.JSONObject());
  assertEquals("400",controller.updateDrugAndStoreBatchInfo(input).getCode());
 }
 @Test void fileValidationAndRoundTrip() throws Exception {
  Path dir=Files.createTempDirectory("demo-upload-test"); FileController files=new FileController(dir.toString());
  try {
   assertThrows(Exception.class,()->files.upload(new MockMultipartFile("file","evil.html","text/html","<script/>".getBytes())));
   assertThrows(Exception.class,()->files.upload(new MockMultipartFile("file","fake.png","image/png","invalid".getBytes())));
   ByteArrayOutputStream bytes=new ByteArrayOutputStream(); ImageIO.write(new BufferedImage(2,2,BufferedImage.TYPE_INT_RGB),"png",bytes);
   Result result=files.upload(new MockMultipartFile("file","../../image.png","image/png",bytes.toByteArray()));
   String name=result.getData().toString().substring(result.getData().toString().lastIndexOf('/')+1);
   MockHttpServletResponse response=new MockHttpServletResponse(); files.download(name,response);
   assertArrayEquals(bytes.toByteArray(),response.getContentAsByteArray());
   response=new MockHttpServletResponse(); files.download("../secret",response); assertEquals(404,response.getStatus());
  } finally { try(java.util.stream.Stream<Path> paths=Files.list(dir)) { paths.forEach(x->{try {Files.delete(x);}catch(IOException e){throw new RuntimeException(e);}}); } Files.delete(dir); }
 }
}
