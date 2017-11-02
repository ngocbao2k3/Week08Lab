package dataaccess;


import domainmodel.Notes;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class NoteDB {

    public int insert(Notes note) throws NotesDBException {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();
            em.persist(note);
            trans.commit();
            return 1;
        } catch (Exception ex) {
            trans.rollback();
            Logger.getLogger(NoteDB.class.getName()).log(Level.SEVERE, "Cannot insert " + note.toString(), ex);
            throw new NotesDBException("Error inserting note");
        } finally {
            em.close();
        }
    }
    
    //converts java Date object to sql Date object
    public java.sql.Date javaDateToSql(java.util.Date date) {
        long javaDateMilisec = date.getTime(); //get javadate in milisec
        java.sql.Date sqlDate = new java.sql.Date(javaDateMilisec); //construct mysql date, from javadate milisec
        
        return sqlDate;
    }
    
    //converts myslq Date object to java Date object
    public java.util.Date sqlDateToJava(java.sql.Date date) {
        long sqlDateMilisec = date.getTime(); //get mysql date in milisec
        java.util.Date javaDate = new java.util.Date(sqlDateMilisec); //construct java date, from javadate milisec
        
        return javaDate;
    }

    public int update(Notes note) throws NotesDBException {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();
            em.merge(note);
            trans.commit();
            return 1;
        } catch (Exception ex) {
            trans.rollback();
            Logger.getLogger(NoteDB.class.getName()).log(Level.SEVERE, "Cannot update " + note.toString(), ex);
            throw new NotesDBException("Error updating note");
        } finally {
            em.close();
        }
    }

    public List<Notes> getAll() throws NotesDBException {
          EntityManager em = DBUtil.getEmFactory().createEntityManager();
        try {
            List<Notes> notes = em.createNamedQuery("Notes.findAll", Notes.class).getResultList();
            return new ArrayList<>(notes);
        } catch (Exception ex) {
            Logger.getLogger(NoteDB.class.getName()).log(Level.SEVERE, "Cannot read notes", ex);
            throw new NotesDBException("Error getting Notes");
        } finally {
            em.close();
        }
    }

    /**
     * Get a single note by their ID.
     *
     * @param noteId The unique ID.
     * @return A Note object if found, null otherwise.
     * @throws NotesDBException
     */
    public Notes getNote(int noteId) throws NotesDBException {
      EntityManager em = DBUtil.getEmFactory().createEntityManager();
       try {
            Notes note = em.find(Notes.class, noteId);
            return note;
        } catch (Exception ex) {
            Logger.getLogger(NoteDB.class.getName()).log(Level.SEVERE, "Cannot read notes", ex);
            throw new NotesDBException("Error getting Notes");
        } finally {
            em.close();
        }
    }

    public int delete(Notes note) throws NotesDBException {
         EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        try {
            trans.begin();
            em.remove(em.merge(note));
            trans.commit();
            return 1;
        } catch (Exception ex) {
            trans.rollback();
            Logger.getLogger(NoteDB.class.getName()).log(Level.SEVERE, "Cannot delete " + note.toString(), ex);
            throw new NotesDBException("Error deleting note");
        } finally {
            em.close();
        }
    }
}
