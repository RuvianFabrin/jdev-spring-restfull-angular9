package com.ru.jdevrestapi.controller;

import com.google.gson.Gson;
import com.ru.jdevrestapi.model.*;
import com.ru.jdevrestapi.repository.TelefoneRepository;
import com.ru.jdevrestapi.repository.UsuarioRepository;
import com.ru.jdevrestapi.service.ImplementacaoUserDetailsService;
import com.ru.jdevrestapi.service.ServiceRelatorio;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;

//@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/usuario")
public class IndexController {

    @Autowired /* Se fosse CDI seria @Inject */
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TelefoneRepository telefoneRepository;

    @Autowired
    private ImplementacaoUserDetailsService implementacaoUserDetailsService;

    @Autowired
    private ServiceRelatorio serviceRelatorio;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /*Isso já é um serviço RESTful*/
    /*@GetMapping(value = "/", produces = "application/json")
    public ResponseEntity init(){
        return new ResponseEntity("Olá Usuario", HttpStatus.OK);
    }*/
   /* @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity listar(
            @RequestParam(value = "nome", required = true,defaultValue = ", digite um nome.") String nome,
            @RequestParam(value = "salario", required = true,defaultValue = ", digite um salario.") String salario
            ){
        System.out.println("Nome:"+nome);
        System.out.println("Salario:"+salario);
        return new ResponseEntity("Olá Usuario "+nome+", seu salario é "+salario, HttpStatus.OK);
    }             */

    /*@GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<List> init(){
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("N");
        usuario.setLogin("L");
        usuario.setSenha("S");

        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNome("N2");
        usuario2.setLogin("L2");
        usuario2.setSenha("S2");

        List<Usuario> usuarios = new ArrayList<Usuario>();
        usuarios.add(usuario);
        usuarios.add(usuario2);

        return ResponseEntity.ok(usuarios);
    }*/

    @GetMapping(value = "/{id}/relatoriopdf", produces = "application/pdf") //mude o produces
    public ResponseEntity<Usuario> relatorio(@PathVariable(value = "id") Long id){

        Optional<Usuario> usuario = usuarioRepository.findById(id);
        /* Suponhando que o retorno seria um relatório */
        /* Aí tem que fazer o retorno byte */
        return ResponseEntity.ok(usuario.get());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Usuario> init(@PathVariable(value = "id") Long id){

        Optional<Usuario> usuario = usuarioRepository.findById(id);

        //return ResponseEntity.ok(new UsuarioDTO(usuario.get())); //DTO
        return ResponseEntity.ok(usuario.get());
    }

    @GetMapping(value = "v1/{id}", produces = "application/json")
    public ResponseEntity<Usuario> initV1(@PathVariable(value = "id") Long id){

        Optional<Usuario> usuario = usuarioRepository.findById(id);

        return ResponseEntity.ok(usuario.get());
    }

    @GetMapping(value = "v2/{id}", produces = "application/json")
    public ResponseEntity<Usuario> initV2(@PathVariable(value = "id") Long id){

        Optional<Usuario> usuario = usuarioRepository.findById(id);

        return ResponseEntity.ok(usuario.get());
    }

    @GetMapping(value = "/{id}", produces = "application/json",headers = "X-API-Version=v1") //No header
    public ResponseEntity<Usuario> initVH1(@PathVariable(value = "id") Long id){

        Optional<Usuario> usuario = usuarioRepository.findById(id);

        return ResponseEntity.ok(usuario.get());
    }

    /* Cache: vamos supor que esse carregamento seja pesado */
    /* Vamos usar cache para aumentar performance */
    @GetMapping(value = "/", produces = "application/json")
    //@Cacheable("cacheusuarios")
    @CacheEvict(value = "cacheusuarios", allEntries = true) // Tira cache de tempo em tempo
    @CachePut("cacheusuarios") // Identifica que tem atualizações e coloca no cache
    public ResponseEntity<Page<Usuario>> usuario() throws InterruptedException {
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("nome"));
        Page<Usuario> page = usuarioRepository.findAll(pageRequest);

        //List<Usuario> list = (List<Usuario>) usuarioRepository.findAll(); //Antes do pageable
        //Thread.sleep(6000); // Segura o código por 6 segundos, simulando lentidão

        return new ResponseEntity<Page<Usuario>>(page,HttpStatus.OK);
    }

    @GetMapping(value = "/page/{pagina}", produces = "application/json")
    //@Cacheable("cacheusuarios")
    @CacheEvict(value = "cacheusuarios", allEntries = true) // Tira cache de tempo em tempo
    @CachePut("cacheusuarios") // Identifica que tem atualizações e coloca no cache
    public ResponseEntity<Page<Usuario>> usuarioPage(@PathVariable("pagina") int pagina ) throws InterruptedException {
        PageRequest pageRequest = PageRequest.of(pagina, 5, Sort.by("nome"));
        Page<Usuario> page = usuarioRepository.findAll(pageRequest);

        //List<Usuario> list = (List<Usuario>) usuarioRepository.findAll(); //Antes do pageable
        //Thread.sleep(6000); // Segura o código por 6 segundos, simulando lentidão

        return new ResponseEntity<Page<Usuario>>(page,HttpStatus.OK);
    }

    @GetMapping(value = "/usuarioPorNome/{nome}", produces = "application/json")
    //@Cacheable("cacheusuarios")
    @CacheEvict(value = "cacheusuarios", allEntries = true) // Tira cache de tempo em tempo
    @CachePut("cacheusuarios") // Identifica que tem atualizações e coloca no cache
    public ResponseEntity<Page<Usuario>> usuarioPorNome(@PathVariable("nome") String nome) throws InterruptedException {

        PageRequest pageRequest = null;
        Page<Usuario> list = null;
        if (nome == null || (nome != null && nome.trim().isEmpty()) || nome.equalsIgnoreCase("undefined")){
            pageRequest = PageRequest.of(0,5, Sort.by("nome"));
            list = usuarioRepository.findAll(pageRequest);
        } else {
            pageRequest = PageRequest.of(0,5, Sort.by("nome"));
            list = usuarioRepository.findUserByNamePage(nome, pageRequest);

        }

        //List<Usuario> list = (List<Usuario>) usuarioRepository.findUserByNome(nome);
        //Thread.sleep(6000); // Segura o código por 6 segundos, simulando lentidão

        return new ResponseEntity<Page<Usuario>>(list,HttpStatus.OK);
    }

    @GetMapping(value = "/usuarioPorNome/{nome}/page/{page}", produces = "application/json")
    //@Cacheable("cacheusuarios")
    @CacheEvict(value = "cacheusuarios", allEntries = true) // Tira cache de tempo em tempo
    @CachePut("cacheusuarios") // Identifica que tem atualizações e coloca no cache
    public ResponseEntity<Page<Usuario>> usuarioPorNomePage(@PathVariable("nome") String nome, @PathVariable("page") int page) throws InterruptedException {

        PageRequest pageRequest = null;
        Page<Usuario> list = null;
        if (nome == null || (nome != null && nome.trim().isEmpty()) || nome.equalsIgnoreCase("undefined")){
            pageRequest = PageRequest.of(page,5, Sort.by("nome"));
            list = usuarioRepository.findAll(pageRequest);
        } else {
            pageRequest = PageRequest.of(page,5, Sort.by("nome"));
            list = usuarioRepository.findUserByNamePage(nome, pageRequest);

        }

        //List<Usuario> list = (List<Usuario>) usuarioRepository.findUserByNome(nome);
        //Thread.sleep(6000); // Segura o código por 6 segundos, simulando lentidão

        return new ResponseEntity<Page<Usuario>>(list,HttpStatus.OK);
    }

    @PostMapping(value = "/", produces = "application/json")
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) throws Exception {

        for (int i = 0; i < usuario.getTelefones().size(); i++) {
            usuario.getTelefones().get(i).setUsuario(usuario);
        }
        if(usuario.getCep()!=null) {
            /* Consumindo API externa */
            URL url = new URL("https://viacep.com.br/ws/" + usuario.getCep() + "/json/");
            URLConnection connection = url.openConnection();
            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            String cep = "";
            StringBuilder jsonCep = new StringBuilder();
            while ((cep = br.readLine()) != null) {
                jsonCep.append(cep);
            }
            Usuario userAux = new Gson().fromJson(jsonCep.toString(), Usuario.class);

            usuario.setCep(userAux.getCep());
            usuario.setLogradouro(userAux.getLogradouro());
            usuario.setComplemento(userAux.getComplemento());
            usuario.setBairro(userAux.getBairro());
            usuario.setLocalidade(userAux.getLocalidade());
            usuario.setUf(userAux.getUf());
            /* Consumindo API externa */
        }

        String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        implementacaoUserDetailsService.insereAcessoPadrao(usuarioSalvo.getId());

        return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
    }


    @PutMapping(value = "/", produces = "application/json")
    public ResponseEntity<Usuario> atualizar(@RequestBody Usuario usuario){

        for (int i = 0; i < usuario.getTelefones().size(); i++) {
            usuario.getTelefones().get(i).setUsuario(usuario);
        }

        Usuario userTemporario = usuarioRepository.findById(usuario.getId()).get();

        if(!userTemporario.getSenha().equals(usuario.getSenha())){
            String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
            usuario.setSenha(senhaCriptografada);
        }
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = "application/text")
    public String deletar(@PathVariable("id") Long id){
        usuarioRepository.deleteById(id);

        return "ok";
    }

    @DeleteMapping(value = "/removerTelefone/{id}", produces = "application/text")
    public String deleteTelefone(@PathVariable("id") Long id){
        telefoneRepository.deleteById(id);
        return "ok";
    }

    @GetMapping(value = "/relatorio", produces = "application/text")
    public ResponseEntity<String> downloadRelatorio(HttpServletRequest request) throws Exception {
        byte[] pdf = serviceRelatorio.gerarRelatorio("relatorio-usuario", new HashMap(),request.getServletContext());
        String base64PDF = "data:application/pdf;base64,"+Base64.encodeBase64String(pdf);
        return new ResponseEntity<String>(base64PDF, HttpStatus.OK);
    }

    @PostMapping(value = "/relatorio/", produces = "application/text")
    public ResponseEntity<String> downloadRelatorioParam(HttpServletRequest request, @RequestBody UserReport userReport) throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dateFormatParam = new SimpleDateFormat("yyyy-MM-dd");

        String dataInicio = dateFormatParam.format(dateFormat.parse(userReport.getDataInicio()));
        String dataFim = dateFormatParam.format(dateFormat.parse(userReport.getDataFim()));

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("DATA_INICIO",dataInicio);
        params.put("DATA_FIM",dataFim);

        byte[] pdf = serviceRelatorio.gerarRelatorio("relatorio-usuario-param", params, request.getServletContext());
        String base64PDF = "data:application/pdf;base64,"+Base64.encodeBase64String(pdf);
        return new ResponseEntity<String>(base64PDF, HttpStatus.OK);
    }

    @GetMapping(value = "/grafico", produces = "application/json")
    public ResponseEntity<UserChart> grafico(){
        UserChart userChart = new UserChart();
        List<String> resultado =  jdbcTemplate.queryForList("SELECT  array_agg(nome) FROM usuario where salario>0 and nome <> '' union all SELECT  cast(array_agg(salario) as character varying[]) FROM usuario where salario>0 and nome <> '';", String.class);
        if (!resultado.isEmpty()){
            userChart.setNome(resultado.get(0).replaceAll("\\{","").replaceAll("\\}",""));
            userChart.setSalario(resultado.get(1).replaceAll("\\{","").replaceAll("\\}",""));
        }
        return new ResponseEntity<UserChart>(userChart, HttpStatus.OK);
    }
}
