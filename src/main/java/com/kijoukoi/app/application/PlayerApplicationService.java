package com.kijoukoi.app.application;

import com.kijoukoi.app.domain.Player;
import com.kijoukoi.app.infrastructure.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

@Service
@Transactional
public class PlayerApplicationService {

    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;

    public PlayerApplicationService(PlayerRepository playerRepository, PasswordEncoder passwordEncoder) {
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public Optional<Player> getPlayerByLogin(String login) {
        return playerRepository.findByLogin(login);
    }

    public Optional<Player> getPlayerById(Long id) {
        return playerRepository.findById(id);
    }

    public Optional<Player> updatePlayer(Long id, Player updatedPlayer) {
        return playerRepository.findById(id).map(player -> {
            player.setRacket(updatedPlayer.getRacket());
            player.setLastRacketUpdateDate(java.time.LocalDateTime.now());
            
            player.getTags().clear();
            if (updatedPlayer.getTags() != null) {
                player.getTags().addAll(updatedPlayer.getTags());
            }

            player.setAge(updatedPlayer.getAge());
            player.setGender(updatedPlayer.getGender());
            player.setRanking(updatedPlayer.getRanking());
            player.setNationality(updatedPlayer.getNationality());

            
            return playerRepository.save(player);
        });
    }

    public Optional<String> getPlayerAvatarDataUri(Long id) {
        return playerRepository.findById(id)
                .map(Player::getAvatar)
                .map(com.kijoukoi.app.domain.Image::getDataUri);
    }

    public byte[] generateExcelTemplate() throws java.io.IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Joueurs");

            // Création de l'en-tête
            Row headerRow = sheet.createRow(0);
            String[] columns = {"Login", "Mot de passe", "Âge", "Nationalité", "Classement", "Genre (M/F/O)"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Ligne d'exemple 1
            Row exampleRow1 = sheet.createRow(1);
            exampleRow1.createCell(0).setCellValue("jean_dupont");
            exampleRow1.createCell(1).setCellValue("password123");
            exampleRow1.createCell(2).setCellValue(25);
            exampleRow1.createCell(3).setCellValue("Française");
            exampleRow1.createCell(4).setCellValue(1500);
            exampleRow1.createCell(5).setCellValue("M");

            // Ligne d'exemple 2
            Row exampleRow2 = sheet.createRow(2);
            exampleRow2.createCell(0).setCellValue("marie_curie");
            exampleRow2.createCell(1).setCellValue("securite!");
            exampleRow2.createCell(2).setCellValue(30);
            exampleRow2.createCell(3).setCellValue("Française");
            exampleRow2.createCell(4).setCellValue(1200);
            exampleRow2.createCell(5).setCellValue("F");

            // Optionnel: largeur de colonnes par défaut
            for (int i = 0; i < columns.length; i++) {
                sheet.setColumnWidth(i, 5000);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    public int importPlayersFromExcel(MultipartFile file) throws Exception {
        int count = 0;
        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            
            for (Row row : sheet) {
                // On ignore la première ligne d'en-tête
                if (row.getRowNum() == 0) continue;
                
                Cell loginCell = row.getCell(0);
                if (loginCell == null || loginCell.getCellType() == CellType.BLANK) {
                    continue; // Fin du fichier ou ligne vide
                }
                
                String login = getCellValueAsString(loginCell);
                if (playerRepository.findByLogin(login).isPresent()) {
                    continue; // Le joueur existe déjà
                }

                Player player = new Player();
                player.setLogin(login);
                
                String password = getCellValueAsString(row.getCell(1));
                if (password != null && !password.trim().isEmpty()) {
                    player.setPassword(passwordEncoder.encode(password));
                } else {
                    player.setPassword(passwordEncoder.encode("default123"));
                }
                
                Cell ageCell = row.getCell(2);
                if (ageCell != null && ageCell.getCellType() == CellType.NUMERIC) {
                    player.setAge((int) ageCell.getNumericCellValue());
                } else {
                    String ageStr = getCellValueAsString(ageCell);
                    if (ageStr != null && !ageStr.isEmpty()) {
                        try { player.setAge(Integer.parseInt(ageStr)); } catch(NumberFormatException ignored) {}
                    }
                }

                player.setNationality(getCellValueAsString(row.getCell(3)));
                
                Cell rankingCell = row.getCell(4);
                if (rankingCell != null && rankingCell.getCellType() == CellType.NUMERIC) {
                    player.setRanking((int) rankingCell.getNumericCellValue());
                } else {
                    String rankingStr = getCellValueAsString(rankingCell);
                    if (rankingStr != null && !rankingStr.isEmpty()) {
                        try { player.setRanking(Integer.parseInt(rankingStr)); } catch(NumberFormatException ignored) {}
                    }
                }

                String gender = getCellValueAsString(row.getCell(5));
                if (gender != null && gender.length() > 0) {
                    player.setGender(gender.substring(0, 1).toUpperCase());
                }
                
                playerRepository.save(player);
                count++;
            }
        }
        return count;
    }
    
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf((int)cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}
