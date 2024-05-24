package com.example.laboratorio6.Controller;

import com.example.laboratorio6.Entity.Mesa;
import com.example.laboratorio6.Entity.Reserva;
import com.example.laboratorio6.Repository.MesaRepository;
import com.example.laboratorio6.Repository.ReservaRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class MesaController {
    final MesaRepository mesaRepository;
    final ReservaRepository reservaRepository;

    public MesaController(MesaRepository mesaRepository,
                          ReservaRepository reservaRepository){
        this.mesaRepository = mesaRepository;
        this.reservaRepository = reservaRepository;
    }

    @GetMapping(value = "/mesas")
    public String mesas(Model model){
        List<Mesa> listaMesas = mesaRepository.findAll();
        model.addAttribute("listaMesas",listaMesas);
        return "mesas";
    }

    @GetMapping(value = "/mesas/agregar")
    public String agregar(@ModelAttribute("mesa") Mesa mesa,
                          Model model){
        model.addAttribute("tipo","agregar");
        return "formMesas";
    }

    @GetMapping(value = "/mesas/editar")
    public String editar(@ModelAttribute("mesa") Mesa mesa,
                         Model model, @RequestParam("id") int id){
        Optional<Mesa> optionalMesa = mesaRepository.findById(id);
        if(optionalMesa.isPresent()){
            mesa = optionalMesa.get();
            model.addAttribute("mesa",mesa);
            model.addAttribute("tipo","editar");
            return "formMesas";
        }else{
            return "redirect:/mesas";
        }
    }

    @PostMapping(value = "/mesas/guardar")
    public String guardar(@ModelAttribute("mesa") @Valid Mesa mesa, BindingResult bindingResult,
                          Model model, RedirectAttributes attr, @RequestParam("tipo") String tipo){
        if(bindingResult.hasErrors()){
            if(!bindingResult.hasFieldErrors("capacidad")) {
                int capacidadActual = mesaRepository.obtenerCapacidadPorId(mesa.getId());
                int diferencia = mesa.getCapacidad() - capacidadActual;
                if(diferencia<0){
                    model.addAttribute("mensajeError","La capacidad no puede ser menor a la capacidad disponible (Capacidad disponible: "+mesa.getDisponibles()+")");
                }
            }
            model.addAttribute("tipo",tipo);
            return "formMesas";
        }else{
            if(mesa.getId()==null){
                mesa.setDisponibles(mesa.getCapacidad());
            }else{
                int capacidadActual = mesaRepository.obtenerCapacidadPorId(mesa.getId());
                int diferencia = mesa.getCapacidad() - capacidadActual;
                if(diferencia<0){
                    model.addAttribute("mensajeError","La capacidad no puede ser menor a la capacidad disponible (Capacidad disponible: "+mesa.getDisponibles()+")");
                    model.addAttribute("tipo",tipo);
                    return "formMesas";
                }
                int nuevoDisponibles = mesa.getDisponibles() + diferencia;
                mesa.setDisponibles(nuevoDisponibles);
            }
            attr.addFlashAttribute("mensaje","Mesa " + (mesa.getId()==null?"agregada ":"editada ") + "exitosamente");
            mesaRepository.save(mesa);
            return "redirect:/mesas";
        }
    }

    @GetMapping(value = "/mesas/eliminar")
    public String eliminar(@RequestParam("id") int id, RedirectAttributes attr){
        Optional<Mesa> optionalMesa = mesaRepository.findById(id);
        if(optionalMesa.isPresent()){
            List<Reserva> listaReservas = reservaRepository.listarReservasPorMesa(id);
            if(listaReservas.isEmpty()){
                mesaRepository.deleteById(id);
                attr.addFlashAttribute("mensaje","Mesa eliminada exitosamente");
            }else{
                attr.addFlashAttribute("mensaje","No se puede eliminar la mesa, ya que ha sido reservada");
            }
        }
        return "redirect:/mesas";
    }
}
