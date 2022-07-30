package ru.kisten.springlearn.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kisten.springlearn.models.Reader;
import ru.kisten.springlearn.repositories.ReadersRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ReadersService {

    private final ReadersRepository readersRepository;

    @Autowired
    public ReadersService(ReadersRepository readersRepository) {
        this.readersRepository = readersRepository;
    }

    public List<Reader> showAll(){
        return readersRepository.findAll();
    }

    public Reader showByIndex(int id) {
        Optional<Reader> reader = readersRepository.findById(id);
        return reader.orElse(null);
    }

    @Transactional
    public void insert(Reader reader) {
        readersRepository.save(reader);
    }

    @Transactional
    public void update(int id, Reader reader) {
        reader.setId(id);
        readersRepository.save(reader);
    }

    @Transactional
    public void delete(int id) {
        readersRepository.deleteById(id);
    }

//    public void insert(Reader reader) {
//
//        jdbcTemplate.update("INSERT INTO Reader (name, age) VALUES (?, ?)", reader.getName(), reader.getAge());
//
//    }
//
//    public void update(int id, Reader reader) {
//
//        jdbcTemplate.update("UPDATE Reader SET name = ?, age = ? WHERE reader_id = ?", reader.getName(), reader.getAge(), id);
//
//    }
//
//    public void delete(int id) {
//
//        jdbcTemplate.update("DELETE FROM Reader WHERE reader_id = ?", id);
//
//    }
//
//    public Reader showByReferenceId(int readerId) {
//
//        return jdbcTemplate.query("SELECT * FROM Reader WHERE reader_id = ?", new Object[]{readerId}, new ReaderMapper()).stream().findAny().orElse(null);
//
//    }
}
