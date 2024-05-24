package com.example.laboratorio6.Controller;

import com.example.laboratorio6.Entity.Mesa;
import com.example.laboratorio6.Entity.Reserva;
import com.example.laboratorio6.Entity.Usuario;
import com.example.laboratorio6.Repository.MesaRepository;
import com.example.laboratorio6.Repository.ReservaRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
public class ReservaController {
    final ReservaRepository reservaRepository;
    final MesaRepository mesaRepository;
    public ReservaController(ReservaRepository reservaRepository,
                             MesaRepository mesaRepository){
        this.reservaRepository = reservaRepository;
        this.mesaRepository = mesaRepository;
    }
    @GetMapping(value = "/reservas")
    public String reservas(Model model, HttpSession session){
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        List<Reserva> listaReservas = usuario.getRol().getRol().equals("CLIENTE")?reservaRepository.listarReservasPorCliente(usuario.getId()):reservaRepository.findAll();
        model.addAttribute("listaReservas",listaReservas);
        return "reservas";
    }

    @GetMapping(value = "/reservas/agregar")
    public String agregar(@ModelAttribute("reserva") Reserva reserva, Model model){
        List<Mesa> listaMesas = mesaRepository.listarMesasConDisponibilidad();
        model.addAttribute("listaMesas", listaMesas);
        return "formReservas";
    }

    @GetMapping(value = "/reservas/cancelar")
    public String cancelar(@RequestParam("id") int id, RedirectAttributes attr){
        Optional<Reserva> optionalReserva = reservaRepository.findById(id);
        if(optionalReserva.isPresent()){
            Reserva reserva = optionalReserva.get();
            mesaRepository.aumentarDisponibilidad(reserva.getMesa().getId());
            reservaRepository.deleteById(id);
            attr.addFlashAttribute("mensaje","Reserva cancelada exitosamente");
        }
        return "redirect:/reservas";
    }

    @PostMapping(value = "/reservas/guardar")
    public String guardar(@ModelAttribute("reserva") @Valid Reserva reserva, BindingResult bindingResult,
                          Model model, RedirectAttributes attr, HttpSession session){
        if(bindingResult.hasErrors()){
            List<Mesa> listaMesas = mesaRepository.listarMesasConDisponibilidad();
            model.addAttribute("listaMesas", listaMesas);
            return "formReservas";
        }else{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            Timestamp fechaInicio = Timestamp.valueOf(LocalDateTime.parse(reserva.getFechainicio()));
            Timestamp fechaFin = Timestamp.valueOf(LocalDateTime.parse(reserva.getFechafin()));
            if(fechaFin.before(fechaInicio)){
                model.addAttribute("mensajeError","La fecha de fin debe ser mayor que la fecha de inicio");
                List<Mesa> listaMesas = mesaRepository.listarMesasConDisponibilidad();
                model.addAttribute("listaMesas", listaMesas);
                return "formReservas";
            }
            attr.addFlashAttribute("mensaje","Reserva agregada exitosamente");
            reserva.setUsuario((Usuario) session.getAttribute("usuario"));
            reservaRepository.save(reserva);
            mesaRepository.disminuirDisponibilidad(reserva.getMesa().getId());
            return "redirect:/reservas";
        }
    }
}
