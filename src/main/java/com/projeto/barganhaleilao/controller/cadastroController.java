package com.projeto.barganhaleilao.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projeto.barganhaleilao.model.CadProduto;
import com.projeto.barganhaleilao.repository.Produtos;

@Controller
@RequestMapping("/cadastroObjeto")
public class cadastroController {

	@Autowired
	private Produtos produtos;

	@RequestMapping("/novo")
	public ModelAndView novo() {
		ModelAndView mv = new ModelAndView("CadastroObjeto");
		mv.addObject(new CadProduto());
		return mv;
	}

	@RequestMapping("{codigo}")
	public ModelAndView editar(@PathVariable ("codigo") CadProduto produto) {
		ModelAndView mv = new ModelAndView("CadastroObjeto");
		mv.addObject(produto);
		return mv;
	}
	
	@RequestMapping(value="{codigo}", method = RequestMethod.DELETE)
	public String excluir(@PathVariable CadProduto codigo, RedirectAttributes attributes) {
		produtos.delete(codigo);
		
		attributes.addFlashAttribute("mensagem", "Título excluído com sucesso!");
		return "redirect:/";
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView salvar(@Validated CadProduto cadProduto, @RequestParam("imagem") MultipartFile file,
			Errors erros) {
		ModelAndView mv = new ModelAndView("CadastroObjeto");
		if (erros.hasErrors()) {
			return mv;
		}
		try {
			FileUploadUtil.saveFile(
					"C:\\Users\\marcos\\OneDrive\\Área de Trabalho\\ProjetoLABSOFT-main\\src\\main\\resources\\static\\images\\upload",
					file.getOriginalFilename(), file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cadProduto.setCaminho("/images/upload/" + file.getOriginalFilename());
		produtos.save(cadProduto);
		mv.addObject("mensagem", "Produto salvo com sucesso!");

		return mv;
	}

}
