package com.apirest.clientes.controller;

import com.apirest.clientes.models.Cliente;
import com.apirest.clientes.service.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ClienteController {

    @Autowired
    private IClienteService clienteService;

    @GetMapping("/clientes")
    public ResponseEntity<List<Cliente>> getAll(){
        return new ResponseEntity<>(clienteService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/clientes/page/{page}")
    public Page<Cliente> listar(@PathVariable Integer page){
        Pageable pageable = PageRequest.of(page, 5);
        return clienteService.findAll(pageable);
    }

    @GetMapping("/clientes/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") long id){
        Cliente cliente = null;
        Map<String, Object> response = new HashMap<>();
        try{
            cliente = clienteService.findById(id);
        }catch(DataAccessException e){
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (cliente == null){
            response.put("mensaje", "Id no registrado en la base de datos");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);

    }

    @PostMapping("/clientes/save")
    public ResponseEntity<?> save(@Valid @RequestBody Cliente cliente, BindingResult result){
        Cliente clienteNuevo = null;
        Map<String, Object> response = new HashMap<>();

       if(result.hasErrors()) {
            /* List<String> errors = new ArrayList<>();

            for (FieldError err: result.getFieldErrors()){
                errors.add("El campo '" + err.getField() + "' " + err.getDefaultMessage());
            }
        }*/

           List<String> errors = result.getFieldErrors()
                   .stream()
                   .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                   .collect(Collectors.toList());

           response.put("errors", errors);
           return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
       }

        try{
            clienteNuevo = clienteService.save(cliente);
        }catch(DataAccessException e){
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("mensaje", "Cliente creado con éxito");
        response.put("cliente", clienteNuevo);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/clientes/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Cliente cliente, BindingResult result, @PathVariable("id") long id){

        Cliente clienteActual = clienteService.findById(id);
        Cliente clienteUpdate = null;
        Map<String, Object> response = new HashMap<>();

        if(result.hasErrors()) {
            /* List<String> errors = new ArrayList<>();

            for (FieldError err: result.getFieldErrors()){
                errors.add("El campo '" + err.getField() + "' " + err.getDefaultMessage());
            }
        }*/

            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (clienteActual == null){
            response.put("mensaje", "Error al editar, el Id no está registrado en la base de datos");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try{
            clienteActual.setNombre(cliente.getNombre());
            clienteActual.setApellido(cliente.getApellido());
            clienteActual.setEmail(cliente.getEmail());
            clienteActual.setCreateAt(cliente.getCreateAt());

            clienteUpdate = clienteService.save(clienteActual);
        }catch(DataAccessException e){
            response.put("mensaje", "Error al realizar el update en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }


        response.put("mensaje", "Cliente actualizado con éxito");
        response.put("cliente", clienteUpdate);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

    }

   @DeleteMapping("/clientes/{id}")
   public ResponseEntity<?> deleteById(@PathVariable("id") long id){
       Map<String, Object> response = new HashMap<>();

       try{
           //para cuando se elime un cliente tambien se elimine su foto de la ruta donde estan almacenadas
           Cliente cliente = clienteService.findById(id);
           String nombreFotoExistente = cliente.getFoto();
           if (nombreFotoExistente != null && nombreFotoExistente.length() > 0){
               Path rutaFotoExistente = Paths.get("uploads").resolve(nombreFotoExistente).toAbsolutePath();
               File archivoFotoExistente = rutaFotoExistente.toFile();
               if (archivoFotoExistente.exists() && archivoFotoExistente.canRead()){
                   archivoFotoExistente.delete();
               }
           }

           clienteService.deleteById(id);
       }catch(DataAccessException e){
           response.put("mensaje", "Error al realizar el delete en la base de datos");
           response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
           return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
       }

       response.put("mensaje", "Cliente eliminado con éxito");
       return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
   }

   //metodo para subir archivo
    @PostMapping("/clientes/upload")
    public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id){
        Map<String, Object> response = new HashMap<>();
        Cliente cliente = clienteService.findById(id);

        if (!archivo.isEmpty()){
            //lo que hacemos es agregarle un indentificador unico a cada archivo que subimos para diferenciarlos(randomUUID)
            String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename().replace(" ", "");
            Path rutaArchivo = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath();

            try{
                //el copy lo que haces es copiar el archivo que subimos al servidor y moverlo a la ruta que creamos
                Files.copy(archivo.getInputStream(), rutaArchivo);
            }catch (IOException e){
                response.put("mensaje", "Error al subir la imagen");
                response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            //antes de actualizar o subir una foto a un cliente validamos que haya una ya existente y la borramos para que solo quede la nueva
            String nombreFotoExistente = cliente.getFoto();
            if (nombreFotoExistente != null && nombreFotoExistente.length() > 0){
                Path rutaFotoExistente = Paths.get("uploads").resolve(nombreFotoExistente).toAbsolutePath();
                File archivoFotoExistente = rutaFotoExistente.toFile();
                if (archivoFotoExistente.exists() && archivoFotoExistente.canRead()){
                    archivoFotoExistente.delete();
                }
            }


            cliente.setFoto(nombreArchivo);
            clienteService.save(cliente);

            response.put("cliente", cliente);
            response.put("mensaje", "Imagen cargada con éxito");
        }

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    //metodo handler para mostrar imagen al usuario en el navegador
    @GetMapping("/uploads/img/{nombreFoto:.+}")
    public ResponseEntity<Resource> verFoto(@PathVariable String nombreFoto){

        Path rutaArchivo = Paths.get("uploads").resolve(nombreFoto).toAbsolutePath();
        Resource recurso = null;

       try{
           recurso = new UrlResource(rutaArchivo.toUri());
       }catch (MalformedURLException e) {
           e.printStackTrace();
       }

       if (!recurso.exists() && !recurso.isReadable()){
           throw new RuntimeException("No se pudo cargar la imagen");
       }

        //para realizar la descarga del archivo
        HttpHeaders cabecera = new HttpHeaders();
        cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"");

        return new ResponseEntity<Resource>(recurso, cabecera, HttpStatus.OK);
    }

}
