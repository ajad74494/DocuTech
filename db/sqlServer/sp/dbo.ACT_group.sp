
/****** Object:  StoredProcedure [dbo].[ACT_group]    Script Date: 18/12/2019 12:41:38 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

	CREATE PROC [dbo].[ACT_group]

	 @tx_action_name		varchar(32)		= NULL	OUTPUT

	, @is_active			int						= 1
	, @id_env_key			int						= NULL	OUTPUT
	, @id_user_mod_key 		int						= NULL	OUTPUT
	, @dtt_mod				datetime				= NULL	OUTPUT

	, @id_state_key			int						= NULL	OUTPUT
	, @tx_state_name		varchar(64)	= NULL	OUTPUT
	, @id_action_key		int						= NULL  OUTPUT

	, @id_event_key			int						= NULL	OUTPUT
	, @id_event_map1_key	int						= NULL	OUTPUT
	, @id_event_map2_key	int						= NULL	OUTPUT
	, @id_event_map3_key	int						= NULL	OUTPUT
	, @id_event_map4_key	int						= NULL	OUTPUT

	, @dtt_valid_from		datetime				= NULL
	, @dtt_valid_to			datetime				= NULL
	, @dtt_as_at			datetime				= NULL
	, @dtt_last_refresh		datetime				= NULL
	, @id_group_key				int								= NULL	OUTPUT
	, @id_group_ver					int								= NULL	OUTPUT
	, @tx_group_name			varchar(64)			= NULL
	, @id_group_type_value_key	int								= NULL
	, @tx_group_type_name		varchar(64)			= NULL
	, @tx_desc					varchar(2048)				= NULL

	-- Mapping variables..
	, @id_map_key				int								= NULL	OUTPUT
	, @id_map_ver				int								= NULL	OUTPUT

	, @id_to_key				int								= NULL				-- e.g role key
	, @id_to_key_ver			int								= NULL				-- e.g role version
	, @id_to_type_key			int								= NULL				-- handles internally

	, @is_primary				int								= NULL
	, @ct_sort_order			int								= NULL

	, @is_sel_data			int						= NULL
	, @tx_log_level			varchar(32)			= NULL
	, @id_log_level			int						= NULL
	, @is_record_time		bit						= NULL
	, @is_print_msg			bit						= NULL
	, @is_persist_msg		bit						= NULL
	, @tx_json_log_msg		varchar(MAX)	= NULL		OUTPUT

AS
BEGIN
	-- GLOBAL VARS --
	DECLARE	  @g_tx_action_name_orig	varchar(32)
			, @g_tx_state_name_orig		varchar(64)

			, @g_id_env_key				int
			, @g_tx_env_name			varchar(256)

			, @g_ct_row					int
			, @g_id_return_status		int

			, @g_id_error_key			int
			, @g_tx_err_msg				varchar(1024)
			, @g_tx_err_msg_tmp			varchar(1024)
			, @g_is_outer_sp			bit

	DECLARE   @g_dt_sys_today			date
			, @g_dt_sys_prev_day		date
			, @g_dt_sys_next_day		date
			, @g_dt_sys_prev_biz_day	date
			, @g_dt_sys_next_biz_day	date

	

	--	DECLARE @g_in_tran		int
	DECLARE	@g_is_ext_txn	bit
	DECLARE @g_is_sp_txn	bit

	SELECT @g_is_ext_txn = 0, @g_is_sp_txn = 0

	DECLARE @g_tmp_int		int

	DECLARE   @g_dtt_log			datetime
	 		, @g_tx_tmp_log_msg		varchar(MAX)
	 		, @g_tx_log_msg			varchar(MAX)

		  	, @g_id_err_num			int
			, @g_id_err_sev			int
			, @g_id_err_state		int
			, @g_tx_sp_name			varchar(128)
			, @g_id_line_num		int
			, @g_id_log_level		int

			, @g_is_record_time		bit
			, @g_is_print_msg		bit
			, @g_is_persist_msg  	bit

			, @g_tx_json_log_msg	varchar(MAX)

	SELECT	@g_tx_json_log_msg = ''

	DECLARE @g_dtt_proc_start		datetime
	DECLARE @g_dtt_proc_end			datetime
	DECLARE @g_dtt_tot_elapsed		int

	DECLARE	@g_dtt_query_start		datetime
	DECLARE	@g_dtt_query_end		datetime
	DECLARE @g_dtt_query_elapsed	int

	DECLARE @g_tmp_tx_tot_time		varchar(255)
	DECLARE @g_tmp_tx_query_time	varchar(255)

	IF ( (@@NESTLEVEL = 1) )
	BEGIN
		IF ( ((@@OPTIONS & 2) != 2) AND (@@TRANCOUNT = 0) )
		BEGIN
			SELECT @g_is_ext_txn = 0
		END
		ELSE
		BEGIN
			SELECT @g_is_ext_txn = 1
		END
	END

	IF (((@tx_log_level IS NULL) OR (@tx_log_level = '?')) OR ((@is_persist_msg IS NULL) OR (@is_persist_msg = -2147483648)) OR ((@is_record_time IS NULL) OR (@is_record_time = -2147483648)))
	BEGIN
		EXEC @g_id_return_status = GET_sp_log_level @tx_sp_name='ACT_group', @tx_log_level=@tx_log_level OUTPUT, @id_log_level=@id_log_level OUTPUT, @is_record_time=@is_record_time OUTPUT, @is_print_msg=@is_print_msg OUTPUT, @is_persist_msg=@is_persist_msg OUTPUT
	END

	SELECT @g_is_record_time = @is_record_time, @g_is_print_msg = @is_print_msg, @g_is_persist_msg	= @is_persist_msg, @g_id_log_level = @id_log_level

	SELECT @g_tx_err_msg = 'Entering[ACT_group] : @tx_action[' + ISNULL(@tx_action_name, '?') + ']'
	SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'ACT_group.sp', @g_id_line_num = 147

	-- TODO_H : [Naz] Move to Function

	IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
	BEGIN
		IF (2 >= @g_id_log_level)
		BEGIN
			IF (@g_is_print_msg = 1)
			BEGIN
				SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'INFO ' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL(@g_tx_err_msg, '?')
				PRINT  @g_tx_tmp_log_msg
			END

			IF ( (@g_is_persist_msg = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
													   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='INFO ', @id_log_level=2
													   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg=@g_tx_err_msg, @tx_json_log_msg = @tx_json_log_msg OUTPUT
			
				IF (@g_id_return_status != 0)
				BEGIN
					RAISERROR ('Error calling ACT_msg_log', 16, 1)
					IF (@g_is_sp_txn = 1)
					BEGIN
						SELECT @g_is_sp_txn = 0
						ROLLBACK TRANSACTION
				
						IF ( (@@NESTLEVEL = 1) )
						BEGIN
							EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
					
							IF (@g_id_return_status != 0)
							BEGIN
								PRINT 'ERROR SAVING JSON LOG MSG!'
							END
						END
					END
					IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
					BEGIN
						-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
				
						IF (30008 = 0)
						BEGIN
							--RAISERROR (@tx_json_log_msg, 10, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
				
						END
						ELSE
						BEGIN
							--RAISERROR (@tx_json_log_msg, 16, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
						END
					END
				
					RETURN 30008
				END
			END
		END
	END

	EXEC @g_id_return_status = GET_environment @id_env_key = @g_id_env_key OUTPUT, @tx_env_name	= @g_tx_env_name OUTPUT, @id_user_key = @id_user_mod_key

	SELECT @g_tx_err_msg = 'Error calling SP -> [GET_ENVIRONMENT] '

	IF (@g_id_return_status != 0)
	BEGIN
		RAISERROR (@g_tx_err_msg, 16, 1)
		IF (@g_is_sp_txn = 1)
		BEGIN
			SELECT @g_is_sp_txn = 0
			ROLLBACK TRANSACTION
	
			IF ( (@@NESTLEVEL = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
		
				IF (@g_id_return_status != 0)
				BEGIN
					PRINT 'ERROR SAVING JSON LOG MSG!'
				END
			END
		END
		IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
		BEGIN
			-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
	
			IF (30001 = 0)
			BEGIN
				--RAISERROR (@tx_json_log_msg, 10, 1)
				EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
	
			END
			ELSE
			BEGIN
				--RAISERROR (@tx_json_log_msg, 16, 1)
				EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
			END
		END
	
		RETURN 30001
	END

	IF (((@dtt_mod IS NULL) OR (@dtt_mod = '01/01/1970')))
	BEGIN
		SELECT @dtt_mod = GETDATE()
	END

	IF (@tx_action_name != 'LOGIN')
	BEGIN
		IF (((@id_user_mod_key IS NULL) OR (@id_user_mod_key = -2147483648)))
		BEGIN
			RAISERROR ('Error:Int [@id_user_mod_key] should not be NULL', 16, 1)
			IF (@g_is_sp_txn = 1)
		BEGIN
			SELECT @g_is_sp_txn = 0
			ROLLBACK TRANSACTION
	
			IF ( (@@NESTLEVEL = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
		
				IF (@g_id_return_status != 0)
				BEGIN
					PRINT 'ERROR SAVING JSON LOG MSG!'
				END
			END
		END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (30011 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 30011
		END
	END

	-- X_SET_NULL_ACTION

	SELECT  @g_tx_action_name_orig = @tx_action_name

	------------------
	--- Validation ---
	------------------

	SELECT @g_tx_err_msg = 'ACTION : ' + @tx_action_name
	SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'ACT_group.sp', @g_id_line_num = 305

	-- TODO_H : [Naz] Move to Function

	IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
	BEGIN
		IF (2 >= @g_id_log_level)
		BEGIN
			IF (@g_is_print_msg = 1)
			BEGIN
				SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'INFO ' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL(@g_tx_err_msg, '?')
				PRINT  @g_tx_tmp_log_msg
			END

			IF ( (@g_is_persist_msg = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
													   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='INFO ', @id_log_level=2
													   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg=@g_tx_err_msg, @tx_json_log_msg = @tx_json_log_msg OUTPUT
			
				IF (@g_id_return_status != 0)
				BEGIN
					RAISERROR ('Error calling ACT_msg_log', 16, 1)
					IF (@g_is_sp_txn = 1)
					BEGIN
						SELECT @g_is_sp_txn = 0
						ROLLBACK TRANSACTION
				
						IF ( (@@NESTLEVEL = 1) )
						BEGIN
							EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
					
							IF (@g_id_return_status != 0)
							BEGIN
								PRINT 'ERROR SAVING JSON LOG MSG!'
							END
						END
					END
					IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
					BEGIN
						-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
				
						IF (30008 = 0)
						BEGIN
							--RAISERROR (@tx_json_log_msg, 10, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
				
						END
						ELSE
						BEGIN
							--RAISERROR (@tx_json_log_msg, 16, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
						END
					END
				
					RETURN 30008
				END
			END
		END
	END

	IF @tx_action_name NOT IN ('NEW', 'UPDATE', 'DELETE', 'MAP_GROUP_TO_ROLE', 'UNMAP_GROUP_TO_ROLE',  'AUDIT_HISTORY' )
	BEGIN
		SELECT @g_tx_err_msg = 'Invalid Action -> ' + ISNULL(@tx_action_name, '?')
        RAISERROR (@g_tx_err_msg, 16, 1)
	IF (@g_is_sp_txn = 1)
	BEGIN
		SELECT @g_is_sp_txn = 0
		ROLLBACK TRANSACTION

		IF ( (@@NESTLEVEL = 1) )
		BEGIN
			EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
	
			IF (@g_id_return_status != 0)
			BEGIN
				PRINT 'ERROR SAVING JSON LOG MSG!'
			END
		END
	END
	IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
	BEGIN
		-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'

		IF (30000 = 0)
		BEGIN
			--RAISERROR (@tx_json_log_msg, 10, 1)
			EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10

		END
		ELSE
		BEGIN
			--RAISERROR (@tx_json_log_msg, 16, 1)
			EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
		END
	END

	RETURN 30000
	END

	IF ( ((@id_event_key IS NULL) OR (@id_event_key = -2147483648)) )
	BEGIN
		SELECT @g_is_outer_sp	= 1, @id_event_map1_key = 0, @id_event_map2_key = 0, @id_event_map3_key = 0, @id_event_map4_key = 0

		EXEC @g_id_return_status = GET_system_key @id_env_key	= @g_id_env_key, @tx_key_name = 'id_event_key', @id_key_value	= @id_event_key OUTPUT, @num_keys = 1
	
		SELECT @g_tx_err_msg = 'Error generating key for id_event_key'
		IF (@g_id_return_status != 0)
		BEGIN
			RAISERROR (@g_tx_err_msg, 16, 1)
			IF (@g_is_sp_txn = 1)
			BEGIN
				SELECT @g_is_sp_txn = 0
				ROLLBACK TRANSACTION
		
				IF ( (@@NESTLEVEL = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
			
					IF (@g_id_return_status != 0)
					BEGIN
						PRINT 'ERROR SAVING JSON LOG MSG!'
					END
				END
			END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (30003 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 30003
		END
	END

	IF ((@tx_action_name = 'NEW'))
	BEGIN

		-- Look up the group type
		/*IF ( ((@id_group_type_value_key IS NULL) OR (@id_group_type_value_key = -2147483648)) AND ((@tx_group_type_name IS NULL) OR (@tx_group_type_name = '?')) )
		BEGIN
			SELECT @g_tx_err_msg = 'Must pass in either _type_id_ or @tx_group_type_name'
			RAISERROR (@g_tx_err_msg, 16, 1)
			IF (@g_is_sp_txn = 1)
			BEGIN
				SELECT @g_is_sp_txn = 0
				ROLLBACK TRANSACTION
		
				IF ( (@@NESTLEVEL = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
			
					IF (@g_id_return_status != 0)
					BEGIN
						PRINT 'ERROR SAVING JSON LOG MSG!'
					END
				END
			END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (99999 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 99999
		END
		ELSE
		BEGIN
			SELECT	@id_group_type_value_key	= id_type_key
				  ,	@tx_group_type_name	= tx_type_name		
			FROM	T_TYPE 
			WHERE	tx_type_category				= ISNULL(NULL	,	tx_type_category)
			AND		tx_type_name					= ISNULL(@tx_group_type_name		,	tx_type_name)
			AND		id_type_key						= ISNULL(@id_group_type_value_key			,	id_type_key)	
			AND		is_active		= ISNULL(@is_active		, is_active)
				--	AND		id_env_key		= ISNULL(@id_env_key	, id_env_key)
	
			SELECT @g_ct_row = @@rowcount, @g_id_error_key = @@error
			SELECT @g_tx_err_msg = 'Error selecting data ' + ' from T_TYPE ' + '@tx_group_type_name [' + CONVERT(varchar, @tx_group_type_name) + '] '
			IF ( (@g_ct_row = 0) OR (@g_id_error_key != 0) )
			BEGIN
				RAISERROR (@g_tx_err_msg, 16, 1)
				IF (@g_is_sp_txn = 1)
			BEGIN
				SELECT @g_is_sp_txn = 0
				ROLLBACK TRANSACTION
		
				IF ( (@@NESTLEVEL = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
			
					IF (@g_id_return_status != 0)
					BEGIN
						PRINT 'ERROR SAVING JSON LOG MSG!'
					END
				END
			END
				IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
				BEGIN
					-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
			
					IF (30008 = 0)
					BEGIN
						--RAISERROR (@tx_json_log_msg, 10, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
			
					END
					ELSE
					BEGIN
						--RAISERROR (@tx_json_log_msg, 16, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
					END
				END
			
				RETURN 30008
			END
		END*/
		IF ( ((@id_group_type_value_key IS NULL) OR (@id_group_type_value_key = -2147483648)) AND ((@tx_group_type_name IS NULL) OR (@tx_group_type_name = '?')) )
		BEGIN
			SELECT @g_tx_err_msg = 'Must pass in either _type_id_ or @tx_group_type_name for getting type value'
			RAISERROR (@g_tx_err_msg, 16, 1)
			IF (@g_is_sp_txn = 1)
			BEGIN
				SELECT @g_is_sp_txn = 0
				ROLLBACK TRANSACTION
		
				IF ( (@@NESTLEVEL = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
			
					IF (@g_id_return_status != 0)
					BEGIN
						PRINT 'ERROR SAVING JSON LOG MSG!'
					END
				END
			END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (99999 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 99999
		END
		ELSE
		BEGIN
			SELECT	@id_group_type_value_key	= id_type_value_key
				  ,	@tx_group_type_name	= tx_type_value
			FROM	V_TYPE_VALUE 
			WHERE	tx_system_name				= ISNULL(NULL	, tx_system_name)
			AND		tx_type_value				= ISNULL(@tx_group_type_name	, tx_type_value)
			AND		id_type_value_key			= ISNULL(@id_group_type_value_key		, id_type_value_key)
			
			SELECT @g_ct_row = @@rowcount, @g_id_error_key = @@error
			SELECT @g_tx_err_msg = 'Error selecting data ' + ' from V_TYPE_VALUE ' + '@tx_group_type_name [' + CONVERT(varchar, @tx_group_type_name) + '] '
			IF ( (@g_ct_row = 0) OR (@g_id_error_key != 0) )
			BEGIN
				RAISERROR (@g_tx_err_msg, 16, 1)
				IF (@g_is_sp_txn = 1)
			BEGIN
				SELECT @g_is_sp_txn = 0
				ROLLBACK TRANSACTION
		
				IF ( (@@NESTLEVEL = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
			
					IF (@g_id_return_status != 0)
					BEGIN
						PRINT 'ERROR SAVING JSON LOG MSG!'
					END
				END
			END
				IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
				BEGIN
					-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
			
					IF (30008 = 0)
					BEGIN
						--RAISERROR (@tx_json_log_msg, 10, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
			
					END
					ELSE
					BEGIN
						--RAISERROR (@tx_json_log_msg, 16, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
					END
				END
			
				RETURN 30008
			END
		END

		EXEC @g_id_return_status = GET_system_key @id_env_key	= @g_id_env_key, @tx_key_name = 'id_group_key', @id_key_value	= @id_group_key OUTPUT, @num_keys = 1
	
		SELECT @g_tx_err_msg = 'Error generating key for id_group_key'
		IF (@g_id_return_status != 0)
		BEGIN
			RAISERROR (@g_tx_err_msg, 16, 1)
			IF (@g_is_sp_txn = 1)
	BEGIN
		SELECT @g_is_sp_txn = 0
		ROLLBACK TRANSACTION

		IF ( (@@NESTLEVEL = 1) )
		BEGIN
			EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
	
			IF (@g_id_return_status != 0)
			BEGIN
				PRINT 'ERROR SAVING JSON LOG MSG!'
			END
		END
	END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (30003 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 30003
		END

		EXEC @g_id_return_status = INS_group
									  @tx_action_name		= @tx_action_name
								
									, @is_active			= @is_active
									, @id_env_key			= @id_env_key				OUTPUT
									, @id_user_mod_key		= @id_user_mod_key			OUTPUT
									, @dtt_mod				= @dtt_mod					OUTPUT
								
									, @id_state_key			= @id_state_key				OUTPUT
									, @tx_state_name		= @tx_state_name			OUTPUT
									, @id_action_key		= @id_action_key			OUTPUT
								
									, @id_event_key			= @id_event_key				OUTPUT
									, @id_event_map1_key	= @id_event_map1_key		OUTPUT
									, @id_event_map2_key	= @id_event_map2_key		OUTPUT
									, @id_event_map3_key	= @id_event_map3_key		OUTPUT
									, @id_event_map4_key	= @id_event_map4_key		OUTPUT
								
									, @dtt_valid_from		= @dtt_valid_from
									, @dtt_valid_to			= @dtt_valid_to
									, @dtt_as_at			= @dtt_as_at
									, @dtt_last_refresh		= @dtt_last_refresh
								
									, @is_sel_data			= @is_sel_data
									, @tx_log_level			= @tx_log_level
									, @id_log_level			= @id_log_level
									, @is_record_time		= @is_record_time
								 	, @is_print_msg  		= @is_print_msg
								 	, @is_persist_msg  		= @is_persist_msg
									, @tx_json_log_msg		= @tx_json_log_msg			OUTPUT
									, @id_group_key				= @id_group_key		OUTPUT
									, @id_group_ver					= @id_group_ver			OUTPUT
									, @tx_group_name			= @tx_group_name
									, @id_group_type_value_key	= @id_group_type_value_key
									, @tx_desc					= @tx_desc

		SELECT @g_tx_err_msg = 'Error calling SP -> [INS_group] '
		IF (@g_id_return_status != 0)
		BEGIN
			RAISERROR (@g_tx_err_msg, 16, 1)
			IF (@g_is_sp_txn = 1)
			BEGIN
				SELECT @g_is_sp_txn = 0
				ROLLBACK TRANSACTION
		
				IF ( (@@NESTLEVEL = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
			
					IF (@g_id_return_status != 0)
					BEGIN
						PRINT 'ERROR SAVING JSON LOG MSG!'
					END
				END
			END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (30001 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 30001
		END

	END

	IF ((@tx_action_name = 'DELETE'))
	BEGIN
		SELECT  @tx_action_name = 'UPDATE', @is_active = 0
	END

	IF @tx_action_name IN ('UPDATE')
	BEGIN
		IF (((@id_group_ver IS NULL) OR (@id_group_ver = -2147483648)))
		BEGIN
			RAISERROR ('Error:Int [@id_group_ver] should not be NULL', 16, 1)
			IF (@g_is_sp_txn = 1)
			BEGIN
				SELECT @g_is_sp_txn = 0
				ROLLBACK TRANSACTION
		
				IF ( (@@NESTLEVEL = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
			
					IF (@g_id_return_status != 0)
					BEGIN
						PRINT 'ERROR SAVING JSON LOG MSG!'
					END
				END
			END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (30011 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 30011
		END

		EXEC @g_id_return_status = UPD_group
							  		@tx_action_name		= @tx_action_name
								
									, @is_active			= @is_active
									, @id_env_key			= @id_env_key				OUTPUT
									, @id_user_mod_key		= @id_user_mod_key			OUTPUT
									, @dtt_mod				= @dtt_mod					OUTPUT
								
									, @id_state_key			= @id_state_key				OUTPUT
									, @tx_state_name		= @tx_state_name			OUTPUT
									, @id_action_key		= @id_action_key			OUTPUT
								
									, @id_event_key			= @id_event_key				OUTPUT
									, @id_event_map1_key	= @id_event_map1_key		OUTPUT
									, @id_event_map2_key	= @id_event_map2_key		OUTPUT
									, @id_event_map3_key	= @id_event_map3_key		OUTPUT
									, @id_event_map4_key	= @id_event_map4_key		OUTPUT
								
									, @dtt_valid_from		= @dtt_valid_from
									, @dtt_valid_to			= @dtt_valid_to
									, @dtt_as_at			= @dtt_as_at
									, @dtt_last_refresh		= @dtt_last_refresh
								
									, @is_sel_data			= @is_sel_data
									, @tx_log_level			= @tx_log_level
									, @id_log_level			= @id_log_level
									, @is_record_time		= @is_record_time
								 	, @is_print_msg  		= @is_print_msg
								 	, @is_persist_msg  		= @is_persist_msg
									, @tx_json_log_msg		= @tx_json_log_msg			OUTPUT
									, @id_group_key				= @id_group_key		OUTPUT
									, @id_group_ver					= @id_group_ver			OUTPUT
									, @tx_group_name			= @tx_group_name
									, @id_group_type_value_key	= @id_group_type_value_key
				   	   				, @tx_desc					= @tx_desc

		SELECT @g_tx_err_msg = 'Error calling SP -> [UPD_group] '
		IF (@g_id_return_status != 0)
		BEGIN
			RAISERROR (@g_tx_err_msg, 16, 1)
			IF (@g_is_sp_txn = 1)
			BEGIN
				SELECT @g_is_sp_txn = 0
				ROLLBACK TRANSACTION
		
				IF ( (@@NESTLEVEL = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
			
					IF (@g_id_return_status != 0)
					BEGIN
						PRINT 'ERROR SAVING JSON LOG MSG!'
					END
				END
			END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (30001 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 30001
		END
	END

	IF ( @tx_action_name IN ('MAP_GROUP_TO_ROLE', 'UNMAP_GROUP_TO_ROLE') )
	BEGIN
		/* IF ( ((@id_group_version IS NULL) OR (@id_group_version = -2147483648)) ) */
		IF ( ((@id_to_key_ver IS NULL) OR (@id_to_key_ver = -2147483648)) )
		BEGIN
			-- Get the latest version of the ROLE
			SELECT	@id_to_key_ver = id_role_ver
			FROM	T_ROLE
			WHERE	id_role_key = @id_to_key   ---We assume passed in @id_to_key is simply @id_role_key
			AND		is_active		= ISNULL(@is_active		, is_active)
				--	AND		id_env_key		= ISNULL(@id_env_key	, id_env_key)

			SELECT @g_ct_row = @@rowcount, @g_id_error_key = @@error
			SELECT @g_tx_err_msg = 'Error selecting data ' + ' from T_GROUP ' + '@id_group_key [' + CONVERT(varchar, @id_group_key) + '] '
			IF ( (@g_ct_row = 0) OR (@g_id_error_key != 0) )
			BEGIN
				RAISERROR (@g_tx_err_msg, 16, 1)
				IF (@g_is_sp_txn = 1)
	BEGIN
		SELECT @g_is_sp_txn = 0
		ROLLBACK TRANSACTION

		IF ( (@@NESTLEVEL = 1) )
		BEGIN
			EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
	
			IF (@g_id_return_status != 0)
			BEGIN
				PRINT 'ERROR SAVING JSON LOG MSG!'
			END
		END
	END
				IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
				BEGIN
					-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
			
					IF (30006 = 0)
					BEGIN
						--RAISERROR (@tx_json_log_msg, 10, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
			
					END
					ELSE
					BEGIN
						--RAISERROR (@tx_json_log_msg, 16, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
					END
				END
			
				RETURN 30006
			END
		END

		--IF we get the @id_version is null, for some reaon I am getting null, this fails UPD_group action below--SHAHID: Pls talk to Naz vai
		IF ( ((@id_group_ver IS NULL) OR (@id_group_ver = -2147483648)) )
		BEGIN
			SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'ACT_group.sp', @g_id_line_num = 927
		
			-- TODO_H : [Naz] Move to Function
		
			IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
			BEGIN
				IF (3 >= @g_id_log_level)
				BEGIN
					IF (@g_is_print_msg = 1)
					BEGIN
						SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'WARN ' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL('TODO CHECK WHY THIS CODE IS HERE!!!', '?')
						PRINT  @g_tx_tmp_log_msg
					END
		
					IF ( (@g_is_persist_msg = 1) )
					BEGIN
						EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
															   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='WARN ', @id_log_level=3
															   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg='TODO CHECK WHY THIS CODE IS HERE!!!', @tx_json_log_msg = @tx_json_log_msg OUTPUT
					
						IF (@g_id_return_status != 0)
						BEGIN
							RAISERROR ('Error calling ACT_msg_log', 16, 1)
							IF (@g_is_sp_txn = 1)
					BEGIN
						SELECT @g_is_sp_txn = 0
						ROLLBACK TRANSACTION
				
						IF ( (@@NESTLEVEL = 1) )
						BEGIN
							EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
					
							IF (@g_id_return_status != 0)
							BEGIN
								PRINT 'ERROR SAVING JSON LOG MSG!'
							END
						END
					END
							IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
							BEGIN
								-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
						
								IF (30008 = 0)
								BEGIN
									--RAISERROR (@tx_json_log_msg, 10, 1)
									EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
						
								END
								ELSE
								BEGIN
									--RAISERROR (@tx_json_log_msg, 16, 1)
									EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
								END
							END
						
							RETURN 30008
						END
					END
				END
			END
			SELECT @id_group_ver = id_group_ver
			FROM T_GROUP
			WHERE id_group_key = @id_group_key
			AND	is_active		= ISNULL(@is_active		, is_active)
				--	AND		id_env_key		= ISNULL(@id_env_key	, id_env_key)

			SELECT @g_ct_row = @@rowcount, @g_id_error_key = @@error
			SELECT @g_tx_err_msg = 'Error selecting data ' + ' from T_GROUP ' + '@id_group_key [' + CONVERT(varchar, @id_group_key) + '] '
			IF ( (@g_ct_row = 0) OR (@g_id_error_key != 0) )
			BEGIN
				RAISERROR (@g_tx_err_msg, 16, 1)
				IF (@g_is_sp_txn = 1)
	BEGIN
		SELECT @g_is_sp_txn = 0
		ROLLBACK TRANSACTION

		IF ( (@@NESTLEVEL = 1) )
		BEGIN
			EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
	
			IF (@g_id_return_status != 0)
			BEGIN
				PRINT 'ERROR SAVING JSON LOG MSG!'
			END
		END
	END
				IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
				BEGIN
					-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
			
					IF (30006 = 0)
					BEGIN
						--RAISERROR (@tx_json_log_msg, 10, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
			
					END
					ELSE
					BEGIN
						--RAISERROR (@tx_json_log_msg, 16, 1)
						EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
					END
				END
			
				RETURN 30006
			END
		END

		IF ((@tx_action_name = 'MAP_GROUP_TO_ROLE'))
		BEGIN
			SELECT  @tx_action_name = 'MAP'
		END
		ELSE
		BEGIN
			SELECT  @tx_action_name = 'UNMAP'
		END

		EXEC @g_id_return_status = ACT_generic_map
									  @tx_action_name		= @tx_action_name
								
									, @is_active			= @is_active
									, @id_env_key			= @id_env_key				OUTPUT
									, @id_user_mod_key		= @id_user_mod_key			OUTPUT
									, @dtt_mod				= @dtt_mod					OUTPUT
								
									, @id_state_key			= @id_state_key				OUTPUT
									, @tx_state_name		= @tx_state_name			OUTPUT
									, @id_action_key		= @id_action_key			OUTPUT
								
									, @id_event_key			= @id_event_key				OUTPUT
									, @id_event_map1_key	= @id_event_map1_key		OUTPUT
									, @id_event_map2_key	= @id_event_map2_key		OUTPUT
									, @id_event_map3_key	= @id_event_map3_key		OUTPUT
									, @id_event_map4_key	= @id_event_map4_key		OUTPUT
								
									, @dtt_valid_from		= @dtt_valid_from
									, @dtt_valid_to			= @dtt_valid_to
									, @dtt_as_at			= @dtt_as_at
									, @dtt_last_refresh		= @dtt_last_refresh
								
									, @is_sel_data			= @is_sel_data
									, @tx_log_level			= @tx_log_level
									, @id_log_level			= @id_log_level
									, @is_record_time		= @is_record_time
								 	, @is_print_msg  		= @is_print_msg
								 	, @is_persist_msg  		= @is_persist_msg
									, @tx_json_log_msg		= @tx_json_log_msg			OUTPUT
									, @id_generic_map_key	= @id_map_key				OUTPUT
									, @id_generic_map_ver	= @id_map_ver				OUTPUT

									, @id_from_key			= @id_group_key
									, @id_from_key_ver		= @id_group_ver
									, @tx_from_type_name	= 'GROUP'

									, @id_to_key			= @id_to_key			-- role key
									, @id_to_key_ver		= @id_to_key_ver		-- role ver
									, @tx_to_type_name		= 'ROLE'

									, @is_primary			= @is_primary
									, @ct_sort_order		= @ct_sort_order

		SELECT @g_tx_err_msg = 'Error calling SP -> [ACT_generic_map] '
		IF (@g_id_return_status != 0)
		BEGIN
			RAISERROR (@g_tx_err_msg, 16, 1)
			IF (@g_is_sp_txn = 1)
			BEGIN
				SELECT @g_is_sp_txn = 0
				ROLLBACK TRANSACTION
		
				IF ( (@@NESTLEVEL = 1) )
				BEGIN
					EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
			
					IF (@g_id_return_status != 0)
					BEGIN
						PRINT 'ERROR SAVING JSON LOG MSG!'
					END
				END
			END
			IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
			BEGIN
				-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
		
				IF (30001 = 0)
				BEGIN
					--RAISERROR (@tx_json_log_msg, 10, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
		
				END
				ELSE
				BEGIN
					--RAISERROR (@tx_json_log_msg, 16, 1)
					EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
				END
			END
		
			RETURN 30001
		END
	END


	IF ((@tx_action_name = 'AUDIT_HISTORY'))
	BEGIN
		SELECT	tx_rs_type = 'RS_TYPE_AUDIT_HISTORY'
					, AUD.id_group_key
					, AUD.id_group_ver
					, AUD.id_user_mod_key
					, AUD.dtt_mod
					, AUD.dtt_mod
					, USR.tx_login_name
			FROM	T_GROUP_AUDIT	AUD
			JOIN	T_USER				USR		ON USR.id_user_key			= AUD.id_user_mod_key
			AND		AUD.id_group_key	=	ISNULL(@id_group_key	, AUD.id_group_key)
			AND		AUD.is_active		= ISNULL(@is_active		, AUD.is_active)
				--	AND		AUD.id_env_key	= ISNULL(@id_env_key	, AUD.id_env_key)
	END

	IF ( (@g_is_record_time = 1) )
		SELECT @g_dtt_proc_end = GETDATE()
	IF ( (@g_is_record_time = 1) )
		SELECT @g_dtt_tot_elapsed  = datediff(ss, @g_dtt_proc_start, @g_dtt_proc_end)

	SELECT @g_tx_err_msg = 'Exiting[ACT_group] : @tx_action[' + ISNULL(@tx_action_name, '?') + ']'
	SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'ACT_group.sp', @g_id_line_num = 1150

	-- TODO_H : [Naz] Move to Function

	IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
	BEGIN
		IF (2 >= @g_id_log_level)
		BEGIN
			IF (@g_is_print_msg = 1)
			BEGIN
				SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'INFO ' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL(@g_tx_err_msg, '?')
				PRINT  @g_tx_tmp_log_msg
			END

			IF ( (@g_is_persist_msg = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
													   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='INFO ', @id_log_level=2
													   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg=@g_tx_err_msg, @tx_json_log_msg = @tx_json_log_msg OUTPUT
			
				IF (@g_id_return_status != 0)
				BEGIN
					RAISERROR ('Error calling ACT_msg_log', 16, 1)
					IF (@g_is_sp_txn = 1)
					BEGIN
						SELECT @g_is_sp_txn = 0
						ROLLBACK TRANSACTION
				
						IF ( (@@NESTLEVEL = 1) )
						BEGIN
							EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
					
							IF (@g_id_return_status != 0)
							BEGIN
								PRINT 'ERROR SAVING JSON LOG MSG!'
							END
						END
					END
					IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
					BEGIN
						-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
				
						IF (30008 = 0)
						BEGIN
							--RAISERROR (@tx_json_log_msg, 10, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
				
						END
						ELSE
						BEGIN
							--RAISERROR (@tx_json_log_msg, 16, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
						END
					END
				
					RETURN 30008
				END
			END
		END
	END

	IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
	BEGIN
		-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'

		IF (0 = 0)
		BEGIN
			--RAISERROR (@tx_json_log_msg, 10, 1)
			EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10

		END
		ELSE
		BEGIN
			--RAISERROR (@tx_json_log_msg, 16, 1)
			EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
		END
	END

	RETURN 0
END
